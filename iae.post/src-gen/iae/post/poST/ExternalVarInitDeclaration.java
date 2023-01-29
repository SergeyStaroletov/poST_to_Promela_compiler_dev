/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>External Var Init Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.ExternalVarInitDeclaration#getVarList <em>Var List</em>}</li>
 *   <li>{@link iae.post.poST.ExternalVarInitDeclaration#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getExternalVarInitDeclaration()
 * @model
 * @generated
 */
public interface ExternalVarInitDeclaration extends EObject
{
  /**
   * Returns the value of the '<em><b>Var List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Var List</em>' containment reference.
   * @see #setVarList(VarList)
   * @see iae.post.poST.PoSTPackage#getExternalVarInitDeclaration_VarList()
   * @model containment="true"
   * @generated
   */
  VarList getVarList();

  /**
   * Sets the value of the '{@link iae.post.poST.ExternalVarInitDeclaration#getVarList <em>Var List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Var List</em>' containment reference.
   * @see #getVarList()
   * @generated
   */
  void setVarList(VarList value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see iae.post.poST.PoSTPackage#getExternalVarInitDeclaration_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link iae.post.poST.ExternalVarInitDeclaration#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

} // ExternalVarInitDeclaration
