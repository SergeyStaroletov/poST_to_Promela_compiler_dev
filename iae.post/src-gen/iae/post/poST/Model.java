/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.Model#getConf <em>Conf</em>}</li>
 *   <li>{@link iae.post.poST.Model#getGlobVars <em>Glob Vars</em>}</li>
 *   <li>{@link iae.post.poST.Model#getPrograms <em>Programs</em>}</li>
 *   <li>{@link iae.post.poST.Model#getFbs <em>Fbs</em>}</li>
 *   <li>{@link iae.post.poST.Model#getFuns <em>Funs</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject
{
  /**
   * Returns the value of the '<em><b>Conf</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Conf</em>' containment reference.
   * @see #setConf(Configuration)
   * @see iae.post.poST.PoSTPackage#getModel_Conf()
   * @model containment="true"
   * @generated
   */
  Configuration getConf();

  /**
   * Sets the value of the '{@link iae.post.poST.Model#getConf <em>Conf</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Conf</em>' containment reference.
   * @see #getConf()
   * @generated
   */
  void setConf(Configuration value);

  /**
   * Returns the value of the '<em><b>Glob Vars</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.GlobalVarDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Glob Vars</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getModel_GlobVars()
   * @model containment="true"
   * @generated
   */
  EList<GlobalVarDeclaration> getGlobVars();

  /**
   * Returns the value of the '<em><b>Programs</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.Program}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Programs</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getModel_Programs()
   * @model containment="true"
   * @generated
   */
  EList<Program> getPrograms();

  /**
   * Returns the value of the '<em><b>Fbs</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.FunctionBlock}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fbs</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getModel_Fbs()
   * @model containment="true"
   * @generated
   */
  EList<FunctionBlock> getFbs();

  /**
   * Returns the value of the '<em><b>Funs</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.Function}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Funs</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getModel_Funs()
   * @model containment="true"
   * @generated
   */
  EList<Function> getFuns();

} // Model
