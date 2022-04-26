import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUnit } from 'app/shared/model/unit.model';
import { getEntities as getUnits } from 'app/entities/unit/unit.reducer';
import { IProviderProduct } from 'app/shared/model/provider-product.model';
import { getEntities as getProviderProducts } from 'app/entities/provider-product/provider-product.reducer';
import { IProductCategory } from 'app/shared/model/product-category.model';
import { getEntities as getProductCategories } from 'app/entities/product-category/product-category.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProductUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const units = useAppSelector(state => state.unit.entities);
  const providerProducts = useAppSelector(state => state.providerProduct.entities);
  const productCategories = useAppSelector(state => state.productCategory.entities);
  const productEntity = useAppSelector(state => state.product.entity);
  const loading = useAppSelector(state => state.product.loading);
  const updating = useAppSelector(state => state.product.updating);
  const updateSuccess = useAppSelector(state => state.product.updateSuccess);
  const handleClose = () => {
    props.history.push('/product' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUnits({}));
    dispatch(getProviderProducts({}));
    dispatch(getProductCategories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...productEntity,
      ...values,
      unit: units.find(it => it.id.toString() === values.unit.toString()),
      providerProduct: providerProducts.find(it => it.id.toString() === values.providerProduct.toString()),
      productCategory: productCategories.find(it => it.id.toString() === values.productCategory.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...productEntity,
          unit: productEntity?.unit?.id,
          providerProduct: productEntity?.providerProduct?.id,
          productCategory: productEntity?.productCategory?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.product.home.createOrEditLabel" data-cy="ProductCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.product.home.createOrEditLabel">Create or edit a Product</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="product-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.product.item')}
                id="product-item"
                name="item"
                data-cy="item"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.product.name')}
                id="product-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.product.description')}
                id="product-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedBlobField
                label={translate('managerProductStockApp.product.image')}
                id="product-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('managerProductStockApp.product.quantity')}
                id="product-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
              />
              <ValidatedField
                id="product-unit"
                name="unit"
                data-cy="unit"
                label={translate('managerProductStockApp.product.unit')}
                type="select"
                required
              >
                <option value="" key="0" />
                {units
                  ? units.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="product-providerProduct"
                name="providerProduct"
                data-cy="providerProduct"
                label={translate('managerProductStockApp.product.providerProduct')}
                type="select"
                required
              >
                <option value="" key="0" />
                {providerProducts
                  ? providerProducts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="product-productCategory"
                name="productCategory"
                data-cy="productCategory"
                label={translate('managerProductStockApp.product.productCategory')}
                type="select"
                required
              >
                <option value="" key="0" />
                {productCategories
                  ? productCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProductUpdate;
