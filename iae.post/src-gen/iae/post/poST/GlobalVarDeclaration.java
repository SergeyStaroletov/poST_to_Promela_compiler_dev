/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Global Var Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.GlobalVarDeclaration#isConst <em>Const</em>}</li>
 *   <li>{@link iae.post.poST.GlobalVarDeclaration#getVarsSimple <em>Vars Simple</em>}</li>
 *   <li>{@link iae.post.poST.GlobalVarDeclaration#getVarsAs <em>Vars As</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getGlobalVarDeclaration()
 * @model
 * @generated
 */
public interface GlobalVarDeclaration extends EObject
{
  /**
   * Returns the value of the '<em><b>Const</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Const</em>' attribute.
   * @see #setConst(boolean)
   * @see iae.post.poST.PoSTPackage#getGlobalVarDeclaration_Const()
   * @model
   * @generated
   */
  boolean isConst();

  /**
   * Sets the value of the '{@link iae.post.poST.GlobalVarDeclaration#isConst <em>Const</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Const</em>' attribute.
   * @see #isConst()
   * @generated
   */
  void setConst(boolean value);

  /**
   * Returns the value of the '<em><b>Vars Simple</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.VarInitDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Vars Simple</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getGlobalVarDeclaration_VarsSimple()
   * @model containment="true"
   * @generated
   */
  EList<VarInitDeclaration> getVarsSimple();

  /**
   * Returns the value of the '<em><b>Vars As</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.GlobalVarInitDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Vars As</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getGlobalVarDeclaration_VarsAs()
   * @model containment="true"
   * @generated
   */
  EList<GlobalVarInitDeclaration> getVarsAs();

} // GlobalVarDeclaration
