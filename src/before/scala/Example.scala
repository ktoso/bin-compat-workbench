class A {
  def it(s: String) = () 
}
  

object RunScala extends App {
  val a = new A
  println("before: " + a.it("hello"))
}
