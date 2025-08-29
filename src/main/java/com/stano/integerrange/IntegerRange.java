package com.stano.integerrange;

import java.util.Iterator;

public final class IntegerRange implements Comparable<IntegerRange>, Iterable<Integer> {
  private final int start;
  private final int end;

  public static IntegerRange of(int start, int end) {
    return new IntegerRange(start, end);
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public int getSize() {
    return end - start + 1;
  }

  public boolean containsValue(int value) {
    return value >= start && value <= end;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IntegerRange integerRange = (IntegerRange)o;

    if (start != integerRange.start) {
      return false;
    }
    if (end != integerRange.end) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = start;
    result = 31 * result + end;
    return result;
  }

  @Override
  public String toString() {
    return String.format("%d - %d", start, end);
  }

  @Override
  public int compareTo(IntegerRange other) {
    if (start < other.start) {
      return -1;
    }

    if (start > other.start) {
      return 1;
    }

    return end - other.end;
  }

  @Override
  public Iterator<Integer> iterator() {
    return new IntegerRangeIterator(this);
  }

  private IntegerRange(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public boolean overlapsWith(IntegerRange other) {
    return this.end >= other.start && this.start <= other.end;
  }
}
