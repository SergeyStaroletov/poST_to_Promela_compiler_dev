/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Single Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.SingleResource#getTasks <em>Tasks</em>}</li>
 *   <li>{@link iae.post.poST.SingleResource#getProgramConfs <em>Program Confs</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getSingleResource()
 * @model
 * @generated
 */
public interface SingleResource extends EObject
{
  /**
   * Returns the value of the '<em><b>Tasks</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.Task}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Tasks</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getSingleResource_Tasks()
   * @model containment="true"
   * @generated
   */
  EList<Task> getTasks();

  /**
   * Returns the value of the '<em><b>Program Confs</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.ProgramConfiguration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Program Confs</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getSingleResource_ProgramConfs()
   * @model containment="true"
   * @generated
   */
  EList<ProgramConfiguration> getProgramConfs();

} // SingleResource
