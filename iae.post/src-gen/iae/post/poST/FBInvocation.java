/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FB Invocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.FBInvocation#getFb <em>Fb</em>}</li>
 *   <li>{@link iae.post.poST.FBInvocation#getArgs <em>Args</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getFBInvocation()
 * @model
 * @generated
 */
public interface FBInvocation extends Statement
{
  /**
   * Returns the value of the '<em><b>Fb</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fb</em>' reference.
   * @see #setFb(SymbolicVariable)
   * @see iae.post.poST.PoSTPackage#getFBInvocation_Fb()
   * @model
   * @generated
   */
  SymbolicVariable getFb();

  /**
   * Sets the value of the '{@link iae.post.poST.FBInvocation#getFb <em>Fb</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Fb</em>' reference.
   * @see #getFb()
   * @generated
   */
  void setFb(SymbolicVariable value);

  /**
   * Returns the value of the '<em><b>Args</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Args</em>' containment reference.
   * @see #setArgs(ParamAssignmentElements)
   * @see iae.post.poST.PoSTPackage#getFBInvocation_Args()
   * @model containment="true"
   * @generated
   */
  ParamAssignmentElements getArgs();

  /**
   * Sets the value of the '{@link iae.post.poST.FBInvocation#getArgs <em>Args</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Args</em>' containment reference.
   * @see #getArgs()
   * @generated
   */
  void setArgs(ParamAssignmentElements value);

} // FBInvocation
