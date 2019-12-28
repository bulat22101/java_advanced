package arrayset;

import java.util.*;

public class ArraySet<E> implements NavigableSet<E> {
    private List<E> arrayList;
    private Comparator<? super E> comparator;

    public ArraySet() {
        this((Comparator<? super E>) null);
    }

    public ArraySet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        arrayList = new ArrayList<>();
    }

    public ArraySet(Collection<E> collection, Comparator<? super E> comparator) {
        this(comparator);
        List<E> temp = new ArrayList<>(collection);
        temp.sort(comparator);
        if (temp.size() > 0) {
            arrayList.add(temp.get(0));
            if (comparator != null) {
                for (int i = 1; i < temp.size(); i++) {
                    if (comparator.compare(temp.get(i), temp.get(i - 1)) != 0) {
                        arrayList.add(temp.get(i));
                    }
                }
            } else {
                Comparable<? super E> e = (Comparable<? super E>) temp.get(0);
                for (int i = 1; i < temp.size(); i++) {
                    if (e.compareTo(temp.get(i)) != 0) {
                        e = (Comparable<? super E>) temp.get(i);
                        arrayList.add(temp.get(i));
                    }
                }
            }
        }
    }

    public ArraySet(Collection<E> collection) {
        this(collection, null);
    }

    private ArraySet(List<E> list, Comparator<? super E> comparator) {
        this.arrayList = list;
        this.comparator = comparator;
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public E first() {
        if (arrayList.isEmpty()) throw new NoSuchElementException();
        return arrayList.get(0);
    }

    @Override
    public E last() {
        if (arrayList.isEmpty()) throw new NoSuchElementException();
        return arrayList.get(arrayList.size() - 1);
    }

    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        return Collections.binarySearch(arrayList, e, comparator) >= 0;
    }

    @Override
    public E lower(E e) {
        if (arrayList.isEmpty()) return null;
        int position = Collections.binarySearch(arrayList, e, comparator);
        if (position < 0) position = -position - 1;
        position--;
        return position < 0 ? null : arrayList.get(position);
    }

    @Override
    public E floor(E e) {
        if (arrayList.isEmpty()) return null;
        int position = Collections.binarySearch(arrayList, e, comparator);
        if (position >= 0) return arrayList.get(position);
        position = -position - 2;
        return position < 0 ? null : arrayList.get(position);
    }

    @Override
    public E ceiling(E e) {
        if (arrayList.isEmpty()) return null;
        int position = Collections.binarySearch(arrayList, e, comparator);
        if (position >= 0) return arrayList.get(position);
        position = -position - 1;
        return position == arrayList.size() ? null : arrayList.get(position);
    }

    @Override
    public E higher(E e) {
        if (arrayList.isEmpty()) return null;
        int position = Collections.binarySearch(arrayList, e, comparator) + 1;
        if (position < 0) position = -position;
        return position == arrayList.size() ? null : arrayList.get(position);
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < arrayList.size();
            }

            @Override
            public E next() {
                return arrayList.get(cursor++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public NavigableSet<E> descendingSet() {
        ArrayList<E> newOrder = new ArrayList<>(arrayList);
        Collections.reverse(newOrder);
        return new ArraySet<>(newOrder, comparator == null ? (Comparator<E>) (o1, o2) -> {
            Comparable<? super E> e = (Comparable<? super E>) o2;
            return e.compareTo(o1);
        } : comparator.reversed());
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<>() {
            int cursor = arrayList.size();

            @Override
            public boolean hasNext() {
                return cursor > 0;
            }

            @Override
            public E next() {
                return arrayList.get(--cursor);
            }
        };
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (comparator == null) {
            Comparable<? super E> e = (Comparable<? super E>) fromElement;
            if (e.compareTo(toElement) > 0) throw new IllegalArgumentException();
        } else {
            if (comparator.compare(fromElement, toElement) > 0) throw new IllegalArgumentException();
        }
        int start = Collections.binarySearch(arrayList, fromElement, comparator);
        if (start < 0) start = -start - 1;
        else if (!fromInclusive) start++;
        int end = Collections.binarySearch(arrayList, toElement, comparator);
        if (end < 0) end = -end - 1;
        else if (toInclusive) end++;
        return new ArraySet<>(start <= end ? arrayList.subList(start, end) : new ArrayList<>(), comparator);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        int position = Collections.binarySearch(arrayList, toElement, comparator);
        if (position < 0) position = -position - 1;
        else if (inclusive) position++;
        return new ArraySet<>(arrayList.subList(0, position), comparator);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        int position = Collections.binarySearch(arrayList, fromElement, comparator);
        if (position < 0) position = -position - 1;
        else if (!inclusive) position++;
        return new ArraySet<>(arrayList.subList(position, arrayList.size()), comparator);
    }

    @Override
    public Object[] toArray() {
        return arrayList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return arrayList.toArray(a);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean result = true;
        for (Object o : c) {
            result &= contains(o);
        }
        return result;
    }

    @Override
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}