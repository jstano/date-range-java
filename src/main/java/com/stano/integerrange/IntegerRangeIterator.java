package com.stano.integerrange;

import java.util.Iterator;

public final class IntegerRangeIterator implements Iterator<Integer> {
  private final Integer lastValue;

  private Integer nextValue;

  public IntegerRangeIterator(IntegerRange integerRange) {
    this.lastValue = integerRange.getEnd();
    this.nextValue = integerRange.getStart();
  }

  @Override
  public boolean hasNext() {
    return nextValue != null;
  }

  @Override
  public Integer next() {
    Integer result = nextValue;

    nextValue++;

    if (nextValue > lastValue) {
      nextValue = null;
    }

    return result;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("The remove method is not supported by IntegerRangeIterator.");
  }
}
