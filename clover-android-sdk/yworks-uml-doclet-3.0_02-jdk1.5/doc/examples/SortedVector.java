package examples;

import java.util.Collection;
import java.util.Comparator;
import java.util.Vector;

/**
 * yDoc example class representing a sorted container structure
 * using {@link java.util.Vector} as a base class.<br>
 * (The source code contains only method stubs, i.e. no implementation.)
 *
 * @author Thomas Behr
 */
public class SortedVector<E> extends Vector<E> implements java.io.Serializable
{

  /**
   * Demonstrating yDoc's filtering capabilites
   *
   * @y.exclude
   */
  public Comparator<E> comparator;
  
  /**
   * Constructor
   *
   * @param comparator   the <i>Comparator</i> to use with binary
   *                     search algorithm
   */
  public SortedVector(Comparator<E> comparator)
  {
  }
  
  /**
   * Constructor<br>
   * Constructs a vector containing the elements of the specified
   * collection. The quick sort algorithm is used to ensure
   * ascending order according to the <i>Comparator</i>.
   *
   * @y.complexity Average case:
   *               <code>O(n log n)</code>, where <code>n</code>
   *               is this container's size (QuickSort complexity)
   * @param collection   the Collection whose elements are to be
   *                     placed into this SortedVector
   * @param comparator   the <i>Comparator</i> to use with binary
   *                     search algorithm
   */
  public SortedVector(Collection<? extends E> collection,
                      Comparator<E> comparator)
  {
  }
  
  /**
   * Insert an element into the vector, sorted in ascending
   * order according to the <i>Comparator</i>.
   * The binary search algorithm is used to determine
   * the position, where the object should be inserted.
   *
   * @y.postcondition The underlying container is still sorted
   *                  in ascending order according to the <i>Comparator</i>
   *                  after the specified Object has been inserted.
   * @y.complexity Average/worst case:
   *               <code>O(log n)</code>, where <code>n</code>
   *               is this container's size (BinarySearch complexity)
   * @param element   the element to insert
   *
   * @return
   *   TRUE, iff the element was successfully inserted.
   *   FALSE, if the element could not be inserted, e.g.
   *   if the vector was already holding an <i>equal</i>
   *   element according to the <i>Comparator</i>
   */
  public boolean insertSorted(E element)
  {
    return false;
  }

    /**
     * Insert elements into the vector, sorted in ascending
     * order according to the <i>Comparator</i>.
     * The binary search algorithm is used to determine
     * the position, where the object should be inserted.
     *
     * @y.postcondition The underlying container is still sorted
     *                  in ascending order according to the <i>Comparator</i>
     *                  after the specified Object has been inserted.
     * @y.complexity Average/worst case:
     *               <code>O(log n)</code>, where <code>n</code>
     *               is this container's size (BinarySearch complexity)
     * @param elements   the elements to insert
     *
     * @return
     *   TRUE, iff the elements was successfully inserted.
     *   FALSE, if the elements could not be inserted, e.g.
     *   if the vector was already holding one or more <i>equal</i>
     *   elements according to the <i>Comparator</i>
     */
  public boolean insertSorted(E... elements)
  {
    return false;
  }

  /**
   * Standard implementation of binary search algorithm.
   *
   * @y.precondition The underlying container has to be sorted
   *                 in ascending order according to the <i>Comparator</i>
   *                 for binary search to work correctly.
   * @y.complexity Average/worst case:
   *               <code>O(log n)</code>, where <code>n</code>
   *               is this container's size
   * @param element   the Object to look for
   */
  public int binarySearch(E element)
  {
    return -1;
  }

  /**
   * Demonstrating yDoc's filtering capabilites
   *
   * @y.exclude
   * @param comparator   the Comparator to use
   */
  public void setComparator(Comparator<E> comparator)
  {
  }

  /**
   * Returns the maximum element of the given sorted vector, according to the
   * natural ordering of its elements. All elements in the sorted vector must
   * implement the <code>Comparable</code> interface. Furthermore, all elements
   * in the sorted vector must be mutually comparable (that is, e1.compareTo(e2)
   * must not throw a <code>ClassCastException</code> for any elements e1 and
   * e2 in the sorted vector).
   *
   * @param data   the <code>SortedVector</code> whose maximum element is to be
   *               determined
   * @return the maximum element of the given sorted vector, according to the
   *         natural ordering of its elements.
   */
  public static <T extends Object & Comparable<? super T>> T
    max(SortedVector<? extends T> data)
  {
      return null;
  }
}
