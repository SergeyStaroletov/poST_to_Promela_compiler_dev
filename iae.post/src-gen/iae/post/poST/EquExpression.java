/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Equ Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.EquExpression#getEquOp <em>Equ Op</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getEquExpression()
 * @model
 * @generated
 */
public interface EquExpression extends CompExpression
{
  /**
   * Returns the value of the '<em><b>Equ Op</b></em>' attribute.
   * The literals are from the enumeration {@link iae.post.poST.EquOperator}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Equ Op</em>' attribute.
   * @see iae.post.poST.EquOperator
   * @see #setEquOp(EquOperator)
   * @see iae.post.poST.PoSTPackage#getEquExpression_EquOp()
   * @model
   * @generated
   */
  EquOperator getEquOp();

  /**
   * Sets the value of the '{@link iae.post.poST.EquExpression#getEquOp <em>Equ Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Equ Op</em>' attribute.
   * @see iae.post.poST.EquOperator
   * @see #getEquOp()
   * @generated
   */
  void setEquOp(EquOperator value);

} // EquExpression
