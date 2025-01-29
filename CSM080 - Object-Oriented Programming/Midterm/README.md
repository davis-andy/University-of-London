# Top Three
>Your task is to provide the body of the class `Top` which implements the interface `Sequence` detailed below.

In this coursework task, you will write a class, named Top, that implements the interface illustrated below:
```java
  public interface Sequence
  {
      void push( String st );
      ArrayList<String> getTopThree();
      void reset();
  }
```

This interface is provided in a file called `Sequence.java`, and a skeleton for the class provided in `Top.java`
for you to complete your work. Both are accessible from the left-side bar.

Your class should in particular implement these methods so that it keeps track of the lowest 3 strings that
have been passed to the method `push`. Here, when we say lowest, we mean according to the `compareTo()` method of
the `String` class; here, when `str1` and `str2` are `Strings`, we consider `str1` lower than `str2` when
`str1.compareTo(str2)` returns a negative value. Strings that are repeats of previously seen strings should be
ignored, and not presented in the output.

**Further details:**
* The `getTopThree()` method should create a **new** object of type `ArrayList`.
* This should be populated with the lowest 3 strings seen so far (via the `push` method).
* These should be in lowest-to-highest order.
* The method should then return the object.
* As repeats should be ignored, no two entries of the returned list should be textually equal to each other.
* If the number of strings that was seen is strictly less than 3, then all of those strings should be present in
the returned list.
* The reset() method should reset the tracking.

**Your class should not maintain a collection of all of the strings seen; the member variable(s) of your class
should store a number of strings that stays below a fixed limit, regardless of how many times the push method
is called.**

As an example, the code fragment

```
    Top t = new Top();
    t.push( "b");
    t.push( "c" );
    t.push( "beeb" );
    System.out.println( t.getTopThree());

    t.reset();
    t.push( "Hiromi" );
    t.push( "Hikari" );
    System.out.println( t.getTopThree());
    
    for( char ch = 'G'; ch <= 'X'; ch++ )
    {
        t.push( "" + ch );
    }
    System.out.println( t.getTopThree());
```

should result in the following being printed to the terminal:

```
    [b, beeb, c]
    [Hikari, Hiromi]
    [G, H, Hikari]
```