package alexander.ivanov;

import java.util.*;
import java.util.function.UnaryOperator;

public class DIYarrayList<T> implements List<T> {
    private static final int DEFAULT_CAPACITY = 10;

    private T[] array;
    private int size;
    private int capacity;

    public DIYarrayList() {
        this(DEFAULT_CAPACITY);
    }

    public DIYarrayList(int capacity) {
        this.capacity = capacity;
        array = (T[]) new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public boolean add(T t) {
        increaseCapacity();
        array[size] = t;
        size++;
        return true;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public T get(int index) {
        Objects.checkIndex(index, size);
        return array[index];
    }

    @Override
    public T set(int index, T element) {
        return array[index] = element;
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public ListIterator<T> listIterator() {
        return new IterList(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new IterList(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public String toString() {
        return "{" +
                "capacity=" + capacity +
                ", size=" + size +
                ", [" + printArrayContent() + "]" +
                "}";
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException("Unsupported!");
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort(array, 0, size, c);
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException("Unsupported!");
    }

    private String printArrayContent() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append(array[i] != null ? array[i].toString() : "null");
            if (i < size-1) {
                res.append(", ");
            }
        }
        return res.toString();
    }

    private void increaseCapacity() {
        if (capacity < size+1) {
            capacity += DEFAULT_CAPACITY;
            T[] tmp = (T[])new Object[capacity];
            for (int i = 0; i < array.length; i++) {
                tmp[i] = array[i];
            }
            array = tmp;
        }
    }

    private class IterList implements ListIterator<T> {
        private int cursor;

        public IterList(int cursor) {
            if (lowBound() || highBound()) {
                throw new IndexOutOfBoundsException();
            }
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return !highBound();
        }

        @Override
        public T next() {
            return hasNext() ? array[nextIndex()] : null;
        }

        @Override
        public boolean hasPrevious() {
            return !lowBound();
        }

        @Override
        public T previous() {
            return hasPrevious() ? array[previousIndex()] : null;
        }

        @Override
        public int nextIndex() {
            return cursor++;
        }

        @Override
        public int previousIndex() {
            return cursor--;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Unsupported!");
        }

        @Override
        public void set(T t) {
            /*System.out.println("DIYarrayList.this = " + DIYarrayList.this + " newVal = " + t);*/
            DIYarrayList.this.set(cursor-1, t);
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException("Unsupported!");
        }

        private boolean lowBound() {
            return cursor < 0;
        }

        private boolean highBound() {
            return cursor >= size;
        }
    }
}