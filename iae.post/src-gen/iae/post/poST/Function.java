/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.Function#getName <em>Name</em>}</li>
 *   <li>{@link iae.post.poST.Function#getType <em>Type</em>}</li>
 *   <li>{@link iae.post.poST.Function#getFunInVars <em>Fun In Vars</em>}</li>
 *   <li>{@link iae.post.poST.Function#getFunOutVars <em>Fun Out Vars</em>}</li>
 *   <li>{@link iae.post.poST.Function#getFunInOutVars <em>Fun In Out Vars</em>}</li>
 *   <li>{@link iae.post.poST.Function#getFunVars <em>Fun Vars</em>}</li>
 *   <li>{@link iae.post.poST.Function#getStatement <em>Statement</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getFunction()
 * @model
 * @generated
 */
public interface Function extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see iae.post.poST.PoSTPackage#getFunction_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link iae.post.poST.Function#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see iae.post.poST.PoSTPackage#getFunction_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link iae.post.poST.Function#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

  /**
   * Returns the value of the '<em><b>Fun In Vars</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.InputVarDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fun In Vars</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getFunction_FunInVars()
   * @model containment="true"
   * @generated
   */
  EList<InputVarDeclaration> getFunInVars();

  /**
   * Returns the value of the '<em><b>Fun Out Vars</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.OutputVarDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fun Out Vars</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getFunction_FunOutVars()
   * @model containment="true"
   * @generated
   */
  EList<OutputVarDeclaration> getFunOutVars();

  /**
   * Returns the value of the '<em><b>Fun In Out Vars</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.InputOutputVarDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fun In Out Vars</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getFunction_FunInOutVars()
   * @model containment="true"
   * @generated
   */
  EList<InputOutputVarDeclaration> getFunInOutVars();

  /**
   * Returns the value of the '<em><b>Fun Vars</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.VarDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fun Vars</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getFunction_FunVars()
   * @model containment="true"
   * @generated
   */
  EList<VarDeclaration> getFunVars();

  /**
   * Returns the value of the '<em><b>Statement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Statement</em>' containment reference.
   * @see #setStatement(StatementList)
   * @see iae.post.poST.PoSTPackage#getFunction_Statement()
   * @model containment="true"
   * @generated
   */
  StatementList getStatement();

  /**
   * Sets the value of the '{@link iae.post.poST.Function#getStatement <em>Statement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Statement</em>' containment reference.
   * @see #getStatement()
   * @generated
   */
  void setStatement(StatementList value);

} // Function
