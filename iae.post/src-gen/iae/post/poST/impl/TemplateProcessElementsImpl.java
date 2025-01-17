/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST.impl;

import iae.post.poST.PoSTPackage;
import iae.post.poST.TemplateProcessAttachVariableConfElement;
import iae.post.poST.TemplateProcessElements;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Template Process Elements</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.impl.TemplateProcessElementsImpl#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TemplateProcessElementsImpl extends MinimalEObjectImpl.Container implements TemplateProcessElements
{
  /**
   * The cached value of the '{@link #getElements() <em>Elements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElements()
   * @generated
   * @ordered
   */
  protected EList<TemplateProcessAttachVariableConfElement> elements;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TemplateProcessElementsImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return PoSTPackage.Literals.TEMPLATE_PROCESS_ELEMENTS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<TemplateProcessAttachVariableConfElement> getElements()
  {
    if (elements == null)
    {
      elements = new EObjectContainmentEList<TemplateProcessAttachVariableConfElement>(TemplateProcessAttachVariableConfElement.class, this, PoSTPackage.TEMPLATE_PROCESS_ELEMENTS__ELEMENTS);
    }
    return elements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case PoSTPackage.TEMPLATE_PROCESS_ELEMENTS__ELEMENTS:
        return ((InternalEList<?>)getElements()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case PoSTPackage.TEMPLATE_PROCESS_ELEMENTS__ELEMENTS:
        return getElements();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case PoSTPackage.TEMPLATE_PROCESS_ELEMENTS__ELEMENTS:
        getElements().clear();
        getElements().addAll((Collection<? extends TemplateProcessAttachVariableConfElement>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case PoSTPackage.TEMPLATE_PROCESS_ELEMENTS__ELEMENTS:
        getElements().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case PoSTPackage.TEMPLATE_PROCESS_ELEMENTS__ELEMENTS:
        return elements != null && !elements.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //TemplateProcessElementsImpl
