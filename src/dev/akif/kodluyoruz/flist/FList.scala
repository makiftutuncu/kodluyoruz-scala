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
  def prepend[AA >: A](element: AA): FList[AA] =
    this match {
      case FNil              => FNonNil(element, FNil)
      case f @ FNonNil(_, _) => FNonNil(element, f)
    }

  /**
   * Concatenates given FList to this one
   *
   * @param that Another FList
   *
   * @tparam AA A supertype of type A to keep type checker happy (FList[AA] = FList[A])
   *
   * @return A new FList by concatenating given FList to this one
   */
  def concat[AA >: A](that: FList[AA]): FList[AA] =
    (this, that) match {
      case (FNil, _)                => that
      case (_, FNil)                => this
      case (FNonNil(head, tail), _) => FNonNil(head, tail.concat(that))
    }

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
  def flatMap[B](flatMapper: A => FList[B]): FList[B] =
    this match {
      case FNil                => FNil
      case FNonNil(head, tail) => flatMapper(head).concat(tail.flatMap(flatMapper))
    }

  /**
   * Size of this FList
   */
  val size: Int =
    this match {
      case FNil             => 0
      case FNonNil(_, tail) => tail.size + 1
    }

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
  def get(index: Int): Option[A] =
    this match {
      case FNil                                           => None
      case FNonNil(_, _)    if index < 0 || index >= size => None
      case FNonNil(head, _) if index == 0                 => Some(head)
      case FNonNil(_, tail)                               => tail.get(index - 1)
    }

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
  def fold[B](initial: B)(step: (B, A) => B): B =
    this match {
      case FNil                => initial
      case FNonNil(head, tail) => tail.fold(step(initial, head))(step)
    }

  /**
   * Filters this FList so only elements that satisfy given predicate remain
   *
   * @param predicate A predicate function
   *
   * @return A new FList containing only elements that satisfy given predicate
   */
  def filter(predicate: A => Boolean): FList[A] =
    this match {
      case FNil                                   => FNil
      case FNonNil(head, tail) if predicate(head) => FNonNil(head, tail.filter(predicate))
      case FNonNil(_, tail)                       => tail.filter(predicate)
    }
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
