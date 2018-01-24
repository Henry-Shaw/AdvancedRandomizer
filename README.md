AdvancedRandomizer
-----
Seeded random number generator for Java

Usage
-----
```java
    ARC4 arc4 = new ARC4("test.\0");
    System.out.println(arc4.nextDouble());
    System.out.println(arc4.nextInt());
    System.out.println(arc4.nextInt(3));
```

Thanks
-----
This project is inspired by [seedrandom](https://github.com/davidbau/seedrandom) 