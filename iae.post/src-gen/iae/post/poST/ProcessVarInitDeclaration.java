/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Var Init Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.ProcessVarInitDeclaration#getVarList <em>Var List</em>}</li>
 *   <li>{@link iae.post.poST.ProcessVarInitDeclaration#getProcess <em>Process</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getProcessVarInitDeclaration()
 * @model
 * @generated
 */
public interface ProcessVarInitDeclaration extends EObject
{
  /**
   * Returns the value of the '<em><b>Var List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Var List</em>' containment reference.
   * @see #setVarList(ProcessVarList)
   * @see iae.post.poST.PoSTPackage#getProcessVarInitDeclaration_VarList()
   * @model containment="true"
   * @generated
   */
  ProcessVarList getVarList();

  /**
   * Sets the value of the '{@link iae.post.poST.ProcessVarInitDeclaration#getVarList <em>Var List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Var List</em>' containment reference.
   * @see #getVarList()
   * @generated
   */
  void setVarList(ProcessVarList value);

  /**
   * Returns the value of the '<em><b>Process</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Process</em>' reference.
   * @see #setProcess(iae.post.poST.Process)
   * @see iae.post.poST.PoSTPackage#getProcessVarInitDeclaration_Process()
   * @model
   * @generated
   */
  iae.post.poST.Process getProcess();

  /**
   * Sets the value of the '{@link iae.post.poST.ProcessVarInitDeclaration#getProcess <em>Process</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Process</em>' reference.
   * @see #getProcess()
   * @generated
   */
  void setProcess(iae.post.poST.Process value);

} // ProcessVarInitDeclaration
