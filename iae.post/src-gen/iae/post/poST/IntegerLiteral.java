/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Integer Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.IntegerLiteral#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getIntegerLiteral()
 * @model
 * @generated
 */
public interface IntegerLiteral extends NumericLiteral
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' containment reference.
   * @see #setValue(SignedInteger)
   * @see iae.post.poST.PoSTPackage#getIntegerLiteral_Value()
   * @model containment="true"
   * @generated
   */
  SignedInteger getValue();

  /**
   * Sets the value of the '{@link iae.post.poST.IntegerLiteral#getValue <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' containment reference.
   * @see #getValue()
   * @generated
   */
  void setValue(SignedInteger value);

} // IntegerLiteral
