package com.stano.integerrange

import spock.lang.Specification
import spock.lang.Unroll

class IntegerRangeSpec extends Specification {
  def "factory method creates range with correct bounds"() {
    when:
    def r = IntegerRange.of(3, 7)

    then:
    r.start == 3
    r.end == 7
  }

  @Unroll
  def "size is computed as inclusive length for #start..#end"() {
    expect:
    IntegerRange.of(start, end).size == expected

    where:
    start | end || expected
    1     | 1   || 1
    1     | 5   || 5
    3     | 3   || 1
    10    | 12  || 3
  }

  def "containsValue checks boundaries and middle"() {
    given:
    def r = IntegerRange.of(2, 4)

    expect:
    r.containsValue(1) == false
    r.containsValue(2)
    r.containsValue(3)
    r.containsValue(4)
    r.containsValue(5) == false
  }

  def "equals and hashCode are based on start and end"() {
    def range13a = IntegerRange.of(1, 3)
    def range13b = IntegerRange.of(1, 3)
    def range14 = IntegerRange.of(1, 4)
    def range23 = IntegerRange.of(2, 3)

    expect:
    range13a.equals(range13a)
    range13a.equals(range13b)
    !range13a.equals(range14)
    !range13a.equals(range23)
    !range13a.equals(null)
    !range13a.equals("abc")

    range13a.hashCode() == range13b.hashCode()
    range13a.hashCode() != range14.hashCode()
  }

  def "toString prints start - end"() {
    expect:
    IntegerRange.of(5, 9).toString() == '5 - 9'
  }

  def "compareTo orders by start then end"() {
    given:
    def a = IntegerRange.of(1, 3)
    def b = IntegerRange.of(2, 2)
    def c = IntegerRange.of(1, 4)
    def d = IntegerRange.of(1, 3)

    expect:
    a.compareTo(b) < 0
    b.compareTo(a) > 0
    a.compareTo(c) < 0
    c.compareTo(a) > 0
    a.compareTo(d) == 0
  }

  def "iterating over the range yields all integers inclusively in order"() {
    given:
    def r = IntegerRange.of(3, 6)

    when:
    def collected = [] as List<Integer>
    for (Integer i : r) {
      collected << i
    }

    then:
    collected == [3, 4, 5, 6]
  }

  def "single-element range iterates just once"() {
    expect:
    (IntegerRange.of(7, 7) as Iterable<Integer>).toList() == [7]
  }

  @Unroll
  def "overlapsWith detects inclusive overlaps for #aStart-#aEnd and #bStart-#bEnd"() {
    given:
    def a = IntegerRange.of(aStart, aEnd)
    def b = IntegerRange.of(bStart, bEnd)

    expect:
    a.overlapsWith(b) == expected
    b.overlapsWith(a) == expected // symmetry

    where:
    aStart | aEnd | bStart | bEnd || expected
    1      | 5    | 5      | 7    || true   // touching at boundary
    1      | 3    | 4      | 6    || false  // separated
    2      | 6    | 3      | 4    || true   // contained overlap
    3      | 3    | 3      | 3    || true   // identical single point
  }
}
