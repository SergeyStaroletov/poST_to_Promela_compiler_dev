/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Program Conf Elements</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.ProgramConfElements#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getProgramConfElements()
 * @model
 * @generated
 */
public interface ProgramConfElements extends EObject
{
  /**
   * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
   * The list contents are of type {@link iae.post.poST.ProgramConfElement}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elements</em>' containment reference list.
   * @see iae.post.poST.PoSTPackage#getProgramConfElements_Elements()
   * @model containment="true"
   * @generated
   */
  EList<ProgramConfElement> getElements();

} // ProgramConfElements
