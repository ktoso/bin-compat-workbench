class A {
  def it(s: String, NOPE: Int) = () 
}
  

object RunScala extends App {
  val a = new A
  println("after: " + a.it("hello", 42))
}
