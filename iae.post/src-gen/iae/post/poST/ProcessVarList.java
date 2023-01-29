/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Var List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.ProcessVarList#getVars <em>Vars</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getProcessVarList()
 * @model
 * @generated
 */
public interface ProcessVarList extends EObject
{
  /**
   * Returns the value of the '<em><b>Vars</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.ProcessVariable}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Vars</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getProcessVarList_Vars()
   * @model containment="true"
   * @generated
   */
  EList<ProcessVariable> getVars();

} // ProcessVarList
