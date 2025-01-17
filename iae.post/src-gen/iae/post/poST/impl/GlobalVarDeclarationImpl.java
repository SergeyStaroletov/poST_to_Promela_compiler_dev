/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST.impl;

import iae.post.poST.GlobalVarDeclaration;
import iae.post.poST.GlobalVarInitDeclaration;
import iae.post.poST.PoSTPackage;
import iae.post.poST.VarInitDeclaration;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Global Var Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.impl.GlobalVarDeclarationImpl#isConst <em>Const</em>}</li>
 *   <li>{@link iae.post.poST.impl.GlobalVarDeclarationImpl#getVarsSimple <em>Vars Simple</em>}</li>
 *   <li>{@link iae.post.poST.impl.GlobalVarDeclarationImpl#getVarsAs <em>Vars As</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GlobalVarDeclarationImpl extends MinimalEObjectImpl.Container implements GlobalVarDeclaration
{
  /**
   * The default value of the '{@link #isConst() <em>Const</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isConst()
   * @generated
   * @ordered
   */
  protected static final boolean CONST_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isConst() <em>Const</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isConst()
   * @generated
   * @ordered
   */
  protected boolean const_ = CONST_EDEFAULT;

  /**
   * The cached value of the '{@link #getVarsSimple() <em>Vars Simple</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVarsSimple()
   * @generated
   * @ordered
   */
  protected EList<VarInitDeclaration> varsSimple;

  /**
   * The cached value of the '{@link #getVarsAs() <em>Vars As</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVarsAs()
   * @generated
   * @ordered
   */
  protected EList<GlobalVarInitDeclaration> varsAs;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GlobalVarDeclarationImpl()
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
    return PoSTPackage.Literals.GLOBAL_VAR_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isConst()
  {
    return const_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setConst(boolean newConst)
  {
    boolean oldConst = const_;
    const_ = newConst;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PoSTPackage.GLOBAL_VAR_DECLARATION__CONST, oldConst, const_));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<VarInitDeclaration> getVarsSimple()
  {
    if (varsSimple == null)
    {
      varsSimple = new EObjectContainmentEList<VarInitDeclaration>(VarInitDeclaration.class, this, PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_SIMPLE);
    }
    return varsSimple;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<GlobalVarInitDeclaration> getVarsAs()
  {
    if (varsAs == null)
    {
      varsAs = new EObjectContainmentEList<GlobalVarInitDeclaration>(GlobalVarInitDeclaration.class, this, PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_AS);
    }
    return varsAs;
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
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_SIMPLE:
        return ((InternalEList<?>)getVarsSimple()).basicRemove(otherEnd, msgs);
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_AS:
        return ((InternalEList<?>)getVarsAs()).basicRemove(otherEnd, msgs);
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
      case PoSTPackage.GLOBAL_VAR_DECLARATION__CONST:
        return isConst();
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_SIMPLE:
        return getVarsSimple();
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_AS:
        return getVarsAs();
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
      case PoSTPackage.GLOBAL_VAR_DECLARATION__CONST:
        setConst((Boolean)newValue);
        return;
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_SIMPLE:
        getVarsSimple().clear();
        getVarsSimple().addAll((Collection<? extends VarInitDeclaration>)newValue);
        return;
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_AS:
        getVarsAs().clear();
        getVarsAs().addAll((Collection<? extends GlobalVarInitDeclaration>)newValue);
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
      case PoSTPackage.GLOBAL_VAR_DECLARATION__CONST:
        setConst(CONST_EDEFAULT);
        return;
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_SIMPLE:
        getVarsSimple().clear();
        return;
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_AS:
        getVarsAs().clear();
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
      case PoSTPackage.GLOBAL_VAR_DECLARATION__CONST:
        return const_ != CONST_EDEFAULT;
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_SIMPLE:
        return varsSimple != null && !varsSimple.isEmpty();
      case PoSTPackage.GLOBAL_VAR_DECLARATION__VARS_AS:
        return varsAs != null && !varsAs.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (const: ");
    result.append(const_);
    result.append(')');
    return result.toString();
  }

} //GlobalVarDeclarationImpl
