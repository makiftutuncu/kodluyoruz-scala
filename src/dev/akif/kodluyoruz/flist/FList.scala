package dev.akif.kodluyoruz.flist

/**
 * An immutable and functional list implementation
 *
 * @tparam A Type of values that can be in this list
 */
sealed trait FList[+A] {
  /**
   * Append given element to this FList
   *
   * @param element Element to append
   *
   * @tparam AA A supertype of type A to keep type checker happy (FList[AA] = FList[A])
   *
   * @return A new FList by appending given element to this FList
   */
  def append[AA >: A](element: AA): FList[AA] =
    this match {
      case FNil                => FNonNil(element, FNil)
      case FNonNil(head, tail) => FNonNil(head, tail.append(element))
    }

  /**
   * Prepends given element to this FList
   *
   * @param element Element to prepend
   *
   * @tparam AA A supertype of type A to keep type checker happy (FList[AA] = FList[A])
   *
   * @return A new FList by prepending given element to this FList
   */
  def prepend[AA >: A](element: AA): FList[AA] = ???

  /**
   * Concatenates given FList to this one
   *
   * @param that Another FList
   *
   * @tparam AA A supertype of type A to keep type checker happy (FList[AA] = FList[A])
   *
   * @return A new FList by concatenating given FList to this one
   */
  def concat[AA >: A](that: FList[AA]): FList[AA] = ???

  /**
   * Creates a new FList by applying given mapper to each element
   *
   * @param mapper A mapper function
   *
   * @tparam B Result type of the mapper function
   *
   * @return A new FList containing mapped elements
   */
  def map[B](mapper: A => B): FList[B] =
    this match {
      case FNil                => FNil
      case FNonNil(head, tail) => FNonNil(mapper(head), tail.map(mapper))
    }

  /**
   * Creates a new FList by applying given flatMapper to each element
   *
   * @param flatMapper A flatMapper function
   *
   * @tparam B Result type of the flatMapper function
   *
   * @return A new FList containing flatMapped elements
   */
  def flatMap[B](flatMapper: A => FList[B]): FList[B] = ???

  /**
   * Size of this FList
   */
  val size: Int = ???

  /**
   * Whether or not this FList is empty
   */
  val isEmpty: Boolean =
    size == 0

  /**
   * Gets an element at given index
   *
   * @param index A 0-based index
   *
   * @return Some(element) if it exists and index is within bounds, None otherwise
   */
  def get(index: Int): Option[A] = ???

  /**
   * Folds this FList over an initial value to produce a result
   *
   * @param initial Some initial value
   * @param step    A step function combining existing result and an element to produce a new result
   *
   * @tparam B Type of result to be produced
   *
   * @return Produced result by folding over this FList
   */
  def fold[B](initial: B)(step: (B, A) => B): B = ???

  /**
   * Filters this FList so only elements that satisfy given predicate remain
   *
   * @param predicate A predicate function
   *
   * @return A new FList containing only elements that satisfy given predicate
   */
  def filter(predicate: A => Boolean): FList[A] = ???
}

/**
 * Companion object of FList
 */
object FList {
  /**
   * Creates an empty FList of given type
   *
   * @tparam A Type of FList
   *
   * @return An empty FList
   */
  def empty[A]: FList[A] = FNil

  /**
   * Creates an FList from given elements
   *
   * @param elements Arbitrary number of elements
   *
   * @tparam A Type of elements
   *
   * @return A new FList containing given elements
   */
  def apply[A](elements: A*): FList[A] =
    elements.foldLeft(empty[A]) {
      case (flist, a) =>
        flist.append(a)
    }

  /**
   * Creates an FList from given Scala List
   *
   * @param list A Scala List
   *
   * @tparam A Type of elements
   *
   * @return A new FList containing elements from given Scala List
   */
  def apply[A](list: List[A]): FList[A] =
    list.foldLeft(empty[A]) {
      case (flist, a) =>
        flist.append(a)
    }
}

/**
 * Non-empty functional list
 *
 * @param head Head element
 * @param tail Tail of this list
 *
 * @tparam A Type of values that can be in this list
 */
final case class FNonNil[A](head: A, tail: FList[A]) extends FList[A]

/**
 * Empty functional list
 */
case object FNil extends FList[Nothing]
