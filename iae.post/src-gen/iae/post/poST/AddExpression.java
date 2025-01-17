/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Add Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.AddExpression#getAddOp <em>Add Op</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getAddExpression()
 * @model
 * @generated
 */
public interface AddExpression extends EquExpression
{
  /**
   * Returns the value of the '<em><b>Add Op</b></em>' attribute.
   * The literals are from the enumeration {@link iae.post.poST.AddOperator}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Add Op</em>' attribute.
   * @see iae.post.poST.AddOperator
   * @see #setAddOp(AddOperator)
   * @see iae.post.poST.PoSTPackage#getAddExpression_AddOp()
   * @model
   * @generated
   */
  AddOperator getAddOp();

  /**
   * Sets the value of the '{@link iae.post.poST.AddExpression#getAddOp <em>Add Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Add Op</em>' attribute.
   * @see iae.post.poST.AddOperator
   * @see #getAddOp()
   * @generated
   */
  void setAddOp(AddOperator value);

} // AddExpression
