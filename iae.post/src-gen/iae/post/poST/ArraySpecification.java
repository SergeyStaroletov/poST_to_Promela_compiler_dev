/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.ArraySpecification#getInterval <em>Interval</em>}</li>
 *   <li>{@link iae.post.poST.ArraySpecification#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getArraySpecification()
 * @model
 * @generated
 */
public interface ArraySpecification extends EObject
{
  /**
   * Returns the value of the '<em><b>Interval</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Interval</em>' containment reference.
   * @see #setInterval(ArrayInterval)
   * @see iae.post.poST.PoSTPackage#getArraySpecification_Interval()
   * @model containment="true"
   * @generated
   */
  ArrayInterval getInterval();

  /**
   * Sets the value of the '{@link iae.post.poST.ArraySpecification#getInterval <em>Interval</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Interval</em>' containment reference.
   * @see #getInterval()
   * @generated
   */
  void setInterval(ArrayInterval value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see iae.post.poST.PoSTPackage#getArraySpecification_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link iae.post.poST.ArraySpecification#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

} // ArraySpecification
