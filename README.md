## Kodluyoruz Scala

**Mehmet Akif Tütüncü** / [Numbrs](https://numbrs.com) / [sahibinden.com](https://sahibinden.com), [VNGRS](https://vngrs.com), Linovi / Scala since 2014

Contact: [akif.dev](https://akif.dev)

## 1. Functional Programming

* Programming with functions based on mathematical function model
  * Not imperative, we don't tell to do things
  * Declarative, we describe what to do and it is interpereted/executed later
* Pure function
  * Output only depends on its input
  * No changes to external state
* Side effect
  * Modifying input or some state
  * Not necessarily producing a result
* Why FP?
  * Immutability
  * Testing
  * Reasoning about code
* Referential transparency - inline all the things!

## 2. Scala Basics

### 2.1. Introduction

* Statically typed
* OOP + FP
* Runs on JVM
* Modern, concise, cool

### 2.2. Hello World

```scala
object Application {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
  }
}
```

### 2.3. Values and Types

```scala
// A variable of type `String`, with no initial value (e.g. null)
// _ is used as a placeholder on definitions
var variableMessage: String = _

variableMessage = "test"

// A value (final variable)
val message: String = "hello"

// Cannot do because val cannot be reassigned
message = "test"

// Cannot do because a value won't be assigned in the future,
// it needs to be assigned now
val value: String = _

// Type can be inferred, no need to provide it explicitly in the definition

val number    = 42         // Type is `Int`
val isEnabled = false      // Type is `Boolean`
val timestamp = 123456789L // Type is `Long`
val separator = '-'        // Type is `Char`
val average   = 123.45     // Type is `Double`

// A method taking 3 parameters and returning `String`
// `z` has a default value (overloading)
def explain(x: String, y: Int, z: Boolean = false): String = {
  // String interpolation
  s"X = $x, Y = $y, Z = $z"
}

println(explain("hello", 5, true))

// Named arguments, notice `z`is not provided so default value will be used
println(explain(x = "hello", 5))

// Since arguments can be named, order may change
println(explain(y = 42, z = true, x = "test"))

// Method return types can also be inferred
// If method body is a single expression, {} are not needed
def add(a: Int, b: Int) = a + b
```

### 2.4. Conditions and Loops

```scala
val score = 42

// if - else if - else block as an expression (produces value)
val state =
  if (score < 33) {
    "Low"
  } else if (score < 66) {
    "Average"
  } else {
    "High"
  }

// match expression (similar but more powerful than switch)
val message =
  state match {
    case "Low"     => "Try again"
    case "Average" => "OK"
    case _         => "Congratulations" // default branch
  }

// while loop with a variable that changes loop condition
var i = 1
while (i <= 10) {
  println(i)
  i += 1
}

// Iterating over [1, 5] range (index based for loop)
for (i <- 1 to 5) println(i)

// Iterating over [6, 11) range
for (j <- 6 until 11) {
  println(j)
}

// Multi dimentional, stepped and conditioned for
for {
  i <- 1 to 20 by 2           // i = 1, 3, 5, ...
  j <- i to (i * 2) if j < 14 // j won't be >= 14, loop will terminate
} {
  print("[" + i + ", " + j + "] ")
}

// for also can be an expression via `yield`
val doubles = (for (i <- 1 to 3) yield i * 2).toList // List(2, 4, 6)
```

### 2.5. Structures

#### 2.5.1. Class

```scala
// Primary constructor in the definition
class Player(val name: String, var score: Int, isNoob: Boolean = true) {
  override def equals(obj: Any): Boolean =
    obj match {
      case that: Player => this.name == that.name && this.score == that.score
      case _            => false
    }
  
  override def hashCode(): Int =
    17 * name.hashCode() * score
  
  def copy(newName: String = name, newScore: Int = score): Player =
    new Player(newName, newScore)
  
  override def toString(): String =
    s"Player($name, $score)"
}

// Instance creation via `new`
val player = new Player("Akif", 0)

// `score` is a field, therefore can be accessed (and modified since it's var)
player.score += 10

// This won't work since it's only a constructor parameter, not a field
println(player.isNoob)

println(player.copy(newName = "Mehmet Akif"))
```

##### 2.5.1.1. Case Class

```scala
// Immutable, therefore `val` fields by default
// Generates all the boilerplate for you
case class Player(name: String, score: Int = 0)

// No need for `new` because generated `apply` is called as `Player.apply(...)`
val player = Player("Akif")

// Generated `copy`
val playerWithHigherScore = player.copy(score = player.score + 10)

// Generated `toString`
println(playerWithHigherScore)

// Also `equals`, `hashCode` and more are generated
```

#### 2.5.2. Object

```scala
// Out-of-the-box singleton
object Utilities {
  val pi = 3.141592653589793
  
  def areaOfCircle(radius: Double): Double = pi * radius * radius
}
```

##### 2.5.2.1. Companion Object

```scala
class Cake(val layers: Int) {
  val description: String = s"$layers layered cake"
  
  def celebrate(): Unit = Cake.celebrateWith(this)
}

// Companion object to `Cake` using same name, otherwise a regular object
object Cake {
  def apply(layers: Int): Cake = new Cake(layers)
  
  def celebrateWith(cake: Cake): Unit =
    println(s"I'm having a ${cake.description} for celebration.")
}

val fruitCake     = new Cake(2) // Regular instance creation
val chocolateCake = Cake(3)     // Short for `Cake.apply(3)`

// Instance method
chocolateCake.celebrate()

// Accessed statically on the object
Cake.celebrateWith(fruitCake)
```

#### 2.5.3. Absrtract Class and Trait

```scala
abstract class Pide() {
  def eat(): Unit
}

trait Meaty {
  val meat: String
}

trait Cheesy {
  val cheese: String
}

case class CheesyMeatPide(override val meat: String,
                          override val cheese: String) extends Pide() with Meaty with Cheesy {
  val name: String = s"$meat pide with $cheese cheese"
  
  override def eat(): Unit = println(s"Eating $name")
}

object VegetarianPide extends Pide() with Cheesy {
  override val cheese: String = "white"
  
  override def eat(): Unit = println(s"I don't eat meat")
}

CheesyMeatPide("ground beef", "white cheddar").eat()

VegetarianPide.eat()
```

### 2.6. Standard Library and Data Structures

#### 2.6.1. Array, List, Set, Map

TODO

#### 2.6.2. Option

TODO

#### 2.6.3. Either

TODO

#### 2.6.4. Try

TODO

#### 2.6.5. Future

TODO
