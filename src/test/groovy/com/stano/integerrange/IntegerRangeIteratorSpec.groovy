package com.stano.integerrange

import spock.lang.Specification

class IntegerRangeIteratorSpec extends Specification {

  def "iterates from start to end inclusive"() {
    given:
    def it = new IntegerRangeIterator(IntegerRange.of(2, 4))

    expect:
    it.hasNext()
    it.next() == 2
    it.hasNext()
    it.next() == 3
    it.hasNext()
    it.next() == 4
    !it.hasNext()
  }

  def "single element iteration"() {
    given:
    def it = new IntegerRangeIterator(IntegerRange.of(7, 7))

    expect:
    it.hasNext()
    it.next() == 7
    !it.hasNext()
  }

  def "remove is not supported"() {
    given:
    def it = new IntegerRangeIterator(IntegerRange.of(1, 1))

    when:
    it.remove()

    then:
    def ex = thrown(UnsupportedOperationException)
    ex.message.contains('not supported')
  }
}
