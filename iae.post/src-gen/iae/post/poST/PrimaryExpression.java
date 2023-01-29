/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Primary Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.PrimaryExpression#getConst <em>Const</em>}</li>
 *   <li>{@link iae.post.poST.PrimaryExpression#getVariable <em>Variable</em>}</li>
 *   <li>{@link iae.post.poST.PrimaryExpression#getArray <em>Array</em>}</li>
 *   <li>{@link iae.post.poST.PrimaryExpression#getProcStatus <em>Proc Status</em>}</li>
 *   <li>{@link iae.post.poST.PrimaryExpression#getFunCall <em>Fun Call</em>}</li>
 *   <li>{@link iae.post.poST.PrimaryExpression#getNestExpr <em>Nest Expr</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getPrimaryExpression()
 * @model
 * @generated
 */
public interface PrimaryExpression extends UnaryExpression
{
  /**
   * Returns the value of the '<em><b>Const</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Const</em>' containment reference.
   * @see #setConst(Constant)
   * @see iae.post.poST.PoSTPackage#getPrimaryExpression_Const()
   * @model containment="true"
   * @generated
   */
  Constant getConst();

  /**
   * Sets the value of the '{@link iae.post.poST.PrimaryExpression#getConst <em>Const</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Const</em>' containment reference.
   * @see #getConst()
   * @generated
   */
  void setConst(Constant value);

  /**
   * Returns the value of the '<em><b>Variable</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variable</em>' reference.
   * @see #setVariable(SymbolicVariable)
   * @see iae.post.poST.PoSTPackage#getPrimaryExpression_Variable()
   * @model
   * @generated
   */
  SymbolicVariable getVariable();

  /**
   * Sets the value of the '{@link iae.post.poST.PrimaryExpression#getVariable <em>Variable</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Variable</em>' reference.
   * @see #getVariable()
   * @generated
   */
  void setVariable(SymbolicVariable value);

  /**
   * Returns the value of the '<em><b>Array</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Array</em>' containment reference.
   * @see #setArray(ArrayVariable)
   * @see iae.post.poST.PoSTPackage#getPrimaryExpression_Array()
   * @model containment="true"
   * @generated
   */
  ArrayVariable getArray();

  /**
   * Sets the value of the '{@link iae.post.poST.PrimaryExpression#getArray <em>Array</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Array</em>' containment reference.
   * @see #getArray()
   * @generated
   */
  void setArray(ArrayVariable value);

  /**
   * Returns the value of the '<em><b>Proc Status</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Proc Status</em>' containment reference.
   * @see #setProcStatus(ProcessStatusExpression)
   * @see iae.post.poST.PoSTPackage#getPrimaryExpression_ProcStatus()
   * @model containment="true"
   * @generated
   */
  ProcessStatusExpression getProcStatus();

  /**
   * Sets the value of the '{@link iae.post.poST.PrimaryExpression#getProcStatus <em>Proc Status</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Proc Status</em>' containment reference.
   * @see #getProcStatus()
   * @generated
   */
  void setProcStatus(ProcessStatusExpression value);

  /**
   * Returns the value of the '<em><b>Fun Call</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fun Call</em>' containment reference.
   * @see #setFunCall(FunctionCall)
   * @see iae.post.poST.PoSTPackage#getPrimaryExpression_FunCall()
   * @model containment="true"
   * @generated
   */
  FunctionCall getFunCall();

  /**
   * Sets the value of the '{@link iae.post.poST.PrimaryExpression#getFunCall <em>Fun Call</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Fun Call</em>' containment reference.
   * @see #getFunCall()
   * @generated
   */
  void setFunCall(FunctionCall value);

  /**
   * Returns the value of the '<em><b>Nest Expr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Nest Expr</em>' containment reference.
   * @see #setNestExpr(Expression)
   * @see iae.post.poST.PoSTPackage#getPrimaryExpression_NestExpr()
   * @model containment="true"
   * @generated
   */
  Expression getNestExpr();

  /**
   * Sets the value of the '{@link iae.post.poST.PrimaryExpression#getNestExpr <em>Nest Expr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Nest Expr</em>' containment reference.
   * @see #getNestExpr()
   * @generated
   */
  void setNestExpr(Expression value);

} // PrimaryExpression
