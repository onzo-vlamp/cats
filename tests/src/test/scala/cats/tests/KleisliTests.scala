package cats
package tests

import cats.arrow.{Arrow, Choice, CommutativeArrow, FunctionK}
import cats.data.{EitherT, Kleisli, Reader}
import cats.functor.{Contravariant, Strong}
import cats.laws.discipline._
import cats.laws.discipline.arbitrary._
import cats.laws.discipline.eq._
import org.scalacheck.Arbitrary
import cats.kernel.laws.GroupLaws
import cats.laws.discipline.{SemigroupKTests, MonoidKTests}

class KleisliTests extends CatsSuite {
  implicit def kleisliEq[F[_], A, B](implicit A: Arbitrary[A], FB: Eq[F[B]]): Eq[Kleisli[F, A, B]] =
    Eq.by[Kleisli[F, A, B], A => F[B]](_.run)

  implicit def readerEq[A, B](implicit A: Arbitrary[A], FB: Eq[Id[B]]): Eq[Reader[A, B]] =
    kleisliEq

  implicit val eitherTEq = EitherT.catsDataEqForEitherT[Kleisli[Option, Int, ?], Unit, Int]
  implicit val eitherTEq2 = EitherT.catsDataEqForEitherT[Reader[Int, ?], Unit, Int]

  implicit val iso = CartesianTests.Isomorphisms.invariant[Kleisli[Option, Int, ?]]
  implicit val iso2 = CartesianTests.Isomorphisms.invariant[Reader[Int, ?]]

  checkAll("Kleisli[Option, Int, Int] with Unit", ApplicativeErrorTests[Kleisli[Option, Int, ?], Unit].applicativeError[Int, Int, Int])
  checkAll("ApplicativeError[Kleisli[Option, Int, Int], Unit]", SerializableTests.serializable(ApplicativeError[Kleisli[Option, Int, ?], Unit]))

  checkAll("Kleisli[Option, Int, Int] with Unit", MonadErrorTests[Kleisli[Option, Int, ?], Unit].monadError[Int, Int, Int])
  checkAll("MonadError[Kleisli[Option, Int, Int], Unit]", SerializableTests.serializable(MonadError[Kleisli[Option, Int, ?], Unit]))

  checkAll("Kleisli[Option, Int, Int]", CartesianTests[Kleisli[Option, Int, ?]].cartesian[Int, Int, Int])
  checkAll("Cartesian[Kleisli[Option, Int, ?]]", SerializableTests.serializable(Cartesian[Kleisli[Option, Int, ?]]))

  checkAll("Kleisli[Option, Int, ?]", CommutativeMonadTests[Kleisli[Option, Int, ?]].commutativeMonad[Int, Int, Int])
  checkAll("CommutativeMonad[Kleisli[Option, Int, ?]]",SerializableTests.serializable(CommutativeMonad[Kleisli[Option, Int, ?]]))

  {
    implicit val catsDataArrowForKleisli = Kleisli.catsDataArrowForKleisli[List]
    checkAll("Kleisli[List, Int, Int]", ArrowTests[Kleisli[List, ?, ?]].arrow[Int, Int, Int, Int, Int, Int])
    checkAll("Arrow[Kleisli[List, ?, ?]]", SerializableTests.serializable(Arrow[Kleisli[List, ?, ?]]))
  }

  {
    implicit val catsDataCommutativeArrowForKleisli = Kleisli.catsDataCommutativeArrowForKleisli[Option]
    checkAll("Kleisli[Option, Int, Int]", CommutativeArrowTests[Kleisli[Option, ?, ?]].commutativeArrow[Int, Int, Int, Int, Int, Int])
    checkAll("CommutativeArrow[Kleisli[Option, ?, ?]]", SerializableTests.serializable(CommutativeArrow[Kleisli[Option, ?, ?]]))
  }

  {
    implicit val catsDataChoiceForKleisli = Kleisli.catsDataChoiceForKleisli[Option]
    checkAll("Kleisli[Option, Int, Int]", ChoiceTests[Kleisli[Option, ?, ?]].choice[Int, Int, Int, Int])
    checkAll("Choice[Kleisli[Option, ?, ?]]", SerializableTests.serializable(Choice[Kleisli[Option, ?, ?]]))
  }

  {
    implicit val catsDataMonadReaderForKleisli = Kleisli.catsDataMonadReaderForKleisli[Option, Int]
    checkAll("Kleisli[Option, Int, Int]", MonadReaderTests[Kleisli[Option, Int, ?], Int].monadReader[Int, Int, Int])
    checkAll("MonadReader[Kleisli[Option, ?, ?], Int]", SerializableTests.serializable(MonadReader[Kleisli[Option, Int, ?], Int]))
  }

  {
    implicit val catsDataMonadReaderForReader = Kleisli.catsDataMonadReaderForKleisliId[Int]
    checkAll("Reader[Int, Int]", MonadReaderTests[Reader[Int, ?], Int].monadReader[Int, Int, Int])
    checkAll("MonadReader[Reader[?, ?], Int]", SerializableTests.serializable(MonadReader[Reader[Int, ?], Int]))
  }

  {
    implicit val catsDataStrongForKleisli = Kleisli.catsDataStrongForKleisli[Option]
    checkAll("Kleisli[Option, Int, Int]", StrongTests[Kleisli[Option, ?, ?]].strong[Int, Int, Int, Int, Int, Int])
    checkAll("Strong[Kleisli[Option, ?, ?]]", SerializableTests.serializable(Strong[Kleisli[Option, ?, ?]]))
  }

  {
    implicit val catsDataFlatMapForKleisli = Kleisli.catsDataFlatMapForKleisli[Option, Int]
    checkAll("Kleisli[Option, Int, Int]", FlatMapTests[Kleisli[Option, Int, ?]].flatMap[Int, Int, Int])
    checkAll("FlatMap[Kleisli[Option, Int, ?]]", SerializableTests.serializable(FlatMap[Kleisli[Option, Int, ?]]))
  }

  {
    implicit val catsDataApplicativeForKleisli = Kleisli.catsDataApplicativeForKleisli[Option, Int]
    checkAll("Kleisli[Option, Int, Int]", ApplicativeTests[Kleisli[Option, Int, ?]].applicative[Int, Int, Int])
    checkAll("Applicative[Kleisli[Option, Int, ?]]", SerializableTests.serializable(Applicative[Kleisli[Option, Int, ?]]))
  }

  {
    implicit val catsDataApplyForKleisli = Kleisli.catsDataApplyForKleisli[Option, Int]
    checkAll("Kleisli[Option, Int, Int]", ApplyTests[Kleisli[Option, Int, ?]].apply[Int, Int, Int])
    checkAll("Apply[Kleisli[Option, Int, ?]]", SerializableTests.serializable(Apply[Kleisli[Option, Int, ?]]))
  }

  {
    implicit val catsDataFunctorForKleisli = Kleisli.catsDataFunctorForKleisli[Option, Int]
    checkAll("Kleisli[Option, Int, Int]", FunctorTests[Kleisli[Option, Int, ?]].functor[Int, Int, Int])
    checkAll("Functor[Kleisli[Option, Int, ?]]", SerializableTests.serializable(Functor[Kleisli[Option, Int, ?]]))
  }

  {
    implicit val catsDataMonoidForKleisli = Kleisli.catsDataMonoidForKleisli[Option, Int, String]
    checkAll("Kleisli[Option, Int, String]", GroupLaws[Kleisli[Option, Int, String]].monoid)
    checkAll("Monoid[Kleisli[Option, Int, String]]", SerializableTests.serializable(catsDataMonoidForKleisli))
  }

  {
    implicit val catsDataSemigroupForKleisli = Kleisli.catsDataSemigroupForKleisli[Option, Int, String]
    checkAll("Kleisli[Option, Int, String]", GroupLaws[Kleisli[Option, Int, String]].semigroup)
    checkAll("Semigroup[Kleisli[Option, Int, String]]", SerializableTests.serializable(catsDataSemigroupForKleisli))
  }

  {
    implicit val catsDataMonoidKForKleisli = Kleisli.catsDataMonoidKForKleisli[Option]
    checkAll("Kleisli[Option, Int, Int]", MonoidKTests[λ[α => Kleisli[Option, α, α]]].monoidK[Int])
    checkAll("MonoidK[λ[α => Kleisli[Option, α, α]]]", SerializableTests.serializable(catsDataMonoidKForKleisli))
  }

  {
    implicit val catsDataSemigroupKForKleisli = Kleisli.catsDataSemigroupKForKleisli[Option]
    checkAll("Kleisli[Option, Int, Int]", SemigroupKTests[λ[α => Kleisli[Option, α, α]]].semigroupK[Int])
    checkAll("SemigroupK[λ[α => Kleisli[Option, α, α]]]", SerializableTests.serializable(catsDataSemigroupKForKleisli))
  }

  checkAll("Reader[Int, Int]", FunctorTests[Reader[Int, ?]].functor[Int, Int, Int])

  checkAll("Kleisli[Option, ?, Int]", ContravariantTests[Kleisli[Option, ?, Int]].contravariant[Int, Int, Int])
  checkAll("Contravariant[Kleisli[Option, ?, Int]]", SerializableTests.serializable(Contravariant[Kleisli[Option, ?, Int]]))

  checkAll("Kleisli[Option, Int, ?]]", MonadTransTests[Kleisli[?[_], Int, ?]].monadTrans[Option, Int, Int])
  checkAll("MonadTrans[Kleisli[?[_], Int, ?]]", SerializableTests.serializable(MonadTrans[Kleisli[?[_], Int, ?]]))


  test("local composes functions") {
    forAll { (f: Int => Option[String], g: Int => Int, i: Int) =>
      f(g(i)) should === (Kleisli.local[Option, String, Int](g)(Kleisli(f)).run(i))
    }
  }

  test("pure consistent with ask") {
    forAll { (i: Int) =>
      Kleisli.pure[Option, Int, Int](i).run(i) should === (Kleisli.ask[Option, Int].run(i))
    }
  }

  test("mapF") {
    forAll { (f: Kleisli[List, Int, Int], t: List[Int] => List[Int], i: Int) =>
      t(f.run(i)) should === (f.mapF(t).run(i))
    }
  }

  test("flatMapF") {
    forAll { (f: Kleisli[List, Int, Int], t: Int => List[Int], i: Int) =>
      f.run(i).flatMap(t) should === (f.flatMapF(t).run(i))
    }
  }

  test("lower") {
    forAll { (f: Kleisli[List, Int, Int], i: Int) =>
      f.run(i) should === (f.lower.run(i).flatten)
    }
  }

  test("tap") {
    forAll { (f: Kleisli[List, Int, String], i: Int) =>
      f.run(i).as(i) should === (f.tap.run(i))
    }
  }

  test("tapWith") {
    forAll { (f: Kleisli[List, Int, String], g: (Int, String) => Boolean, i: Int) =>
      f.run(i).map(s => g(i, s)) should === (f.tapWith(g).run(i))
    }
  }

  test("apply") {
    forAll { (f: Kleisli[List, Int, Int], i: Int) =>
      f.run(i) should === (f(i))
    }
  }

  test("traverse") {
    forAll { (f: Kleisli[List, Int, Int], i: Int) =>
      f.traverse(Some(i): Option[Int]) should === ((Some(i): Option[Int]).traverse(f(_)))
    }
  }

  test("lift") {
    val f = Kleisli { (x: Int) => (Some(x + 1): Option[Int]) }
    val l = f.lift[List]
    (List(1, 2, 3) >>= l.run) should === (List(Some(2), Some(3), Some(4)))
  }

  test("transform") {
    val opt = Kleisli { (x: Int) => Option(x.toDouble) }
    val optToList = λ[FunctionK[Option,List]](_.toList)
    val list = opt.transform(optToList)

    val is = 0.to(10).toList
    is.map(list.run) should === (is.map(Kleisli { (x: Int) => List(x.toDouble) }.run))
  }

  test("local") {
    case class Config(i: Int, s: String)

    val kint = Kleisli { (x: Int) => Option(x.toDouble) }
    val kconfig1 = kint.local[Config](_.i)
    val kconfig2 = Kleisli { (c: Config) => Option(c.i.toDouble) }

    val config = Config(0, "cats")
    kconfig1.run(config) should === (kconfig2.run(config))
  }

  /**
   * Testing that implicit resolution works. If it compiles, the "test" passes.
   */
  object ImplicitResolution {
    // F is List
    Functor[Kleisli[List, Int, ?]]
    Apply[Kleisli[List, Int, ?]]
    Applicative[Kleisli[List, Int, ?]]
    Monad[Kleisli[List, Int, ?]]
    MonadReader[Kleisli[List, Int, ?], Int]
    Monoid[Kleisli[List, Int, String]]
    MonoidK[λ[α => Kleisli[List, α, α]]]
    Arrow[Kleisli[List, ?, ?]]
    Choice[Kleisli[List, ?, ?]]
    Strong[Kleisli[List, ?, ?]]
    FlatMap[Kleisli[List, Int, ?]]
    Semigroup[Kleisli[List, Int, String]]
    SemigroupK[λ[α => Kleisli[List, α, α]]]

    // F is Id
    Functor[Kleisli[Id, Int, ?]]
    Apply[Kleisli[Id, Int, ?]]
    Applicative[Kleisli[Id, Int, ?]]
    Monad[Kleisli[Id, Int, ?]]
    MonadReader[Kleisli[Id, Int, ?], Int]
    Monoid[Kleisli[Id, Int, String]]
    MonoidK[λ[α => Kleisli[Id, α, α]]]
    Arrow[Kleisli[Id, ?, ?]]
    CommutativeArrow[Kleisli[Id, ?, ?]]
    Choice[Kleisli[Id, ?, ?]]
    Strong[Kleisli[Id, ?, ?]]
    FlatMap[Kleisli[Id, Int, ?]]
    Semigroup[Kleisli[Id, Int, String]]
    SemigroupK[λ[α => Kleisli[Id, α, α]]]

    // using Reader alias instead of Kleisli with Id as F
    Functor[Reader[Int, ?]]
    Apply[Reader[Int, ?]]
    Applicative[Reader[Int, ?]]
    Monad[Reader[Int, ?]]
    MonadReader[Reader[Int, ?], Int]
    Monoid[Reader[Int, String]]
    MonoidK[λ[α => Reader[α, α]]]
    Arrow[Reader[?, ?]]
    CommutativeArrow[Reader[?, ?]]
    Choice[Reader[?, ?]]
    Strong[Reader[?, ?]]
    FlatMap[Reader[Int, ?]]
    Semigroup[Reader[Int, String]]
    SemigroupK[λ[α => Reader[α, α]]]

    // using IntReader alias instead of Kleisli with Id as F and A as Int
    type IntReader[A] = Reader[Int, A]
    Functor[IntReader]
    Apply[IntReader]
    Applicative[IntReader]
    Monad[IntReader]
    MonadReader[IntReader, Int]
    Monoid[IntReader[String]]
    FlatMap[IntReader]
    Semigroup[IntReader[String]]

    ApplicativeError[Kleisli[cats.data.Validated[Unit, ?], Int, ?], Unit]
    ApplicativeError[Kleisli[Option, Int, ?], Unit]
    MonadError[Kleisli[Option, Int, ?], Unit]

    MonadTrans[Kleisli[?[_], Int, ?]]
  }
}
