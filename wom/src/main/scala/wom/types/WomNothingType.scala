package wom.types

import wom.values.{WomOptionalValue, WomValue}
import scala.util.{Success, Try}

/**
  * For when we need to assign a type to a composite wom value that was created empty, may I present to you the least
  * (and yet at the same time, most) interesting of all the types, the WomNothingType!
  */
case object WomNothingType extends WomType {
  override protected def coercion: PartialFunction[Any, WomValue] = {
    case WomOptionalValue(WomNothingType, None) => WomOptionalValue(WomNothingType, None)
  }
  override def stableName: String = "Nothing"

  override def add(rhs: WomType): Try[WomType] = rhs match {
    case WomStringType => Success(WomNothingType)
    case _ => invalid(s"$this + $rhs")
  }
}
