/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Selection Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.SelectionStatement#getElseStatement <em>Else Statement</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getSelectionStatement()
 * @model
 * @generated
 */
public interface SelectionStatement extends Statement
{
  /**
   * Returns the value of the '<em><b>Else Statement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Else Statement</em>' containment reference.
   * @see #setElseStatement(StatementList)
   * @see iae.post.poST.PoSTPackage#getSelectionStatement_ElseStatement()
   * @model containment="true"
   * @generated
   */
  StatementList getElseStatement();

  /**
   * Sets the value of the '{@link iae.post.poST.SelectionStatement#getElseStatement <em>Else Statement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Else Statement</em>' containment reference.
   * @see #getElseStatement()
   * @generated
   */
  void setElseStatement(StatementList value);

} // SelectionStatement
