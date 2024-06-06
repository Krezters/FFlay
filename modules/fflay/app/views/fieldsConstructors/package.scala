package views

import views.html.helper.FieldConstructor

package object fieldsConstructors {
  val textFieldConstructor: FieldConstructor = FieldConstructor(f =>
    views.html.fieldsConstructors.textFieldConstructor(f)
  )

  val passwordFieldConstructor: FieldConstructor = FieldConstructor(f =>
    views.html.fieldsConstructors.passwordFieldConstructor(f)
  )

  val numberFieldConstructor: FieldConstructor = FieldConstructor(f =>
    views.html.fieldsConstructors.numberFieldConstructor(f)
  )

  val hiddenFieldConstructor: FieldConstructor = FieldConstructor(f =>
    views.html.fieldsConstructors.hiddenFieldConstructor(f)
  )

  val fileFieldConstructor: FieldConstructor = FieldConstructor(f =>
    views.html.fieldsConstructors.fileFieldConstructor(f)
  )

  val checkboxFieldConstructor: FieldConstructor = FieldConstructor(f =>
    views.html.fieldsConstructors.checkboxFieldConstructor(f)
  )
}
