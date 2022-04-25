import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IBatchProduct } from 'app/shared/model/batch-product.model';
import { getEntities as getBatchProducts } from 'app/entities/batch-product/batch-product.reducer';
import { IProviderProductPrice } from 'app/shared/model/provider-product-price.model';
import { getEntities as getProviderProductPrices } from 'app/entities/provider-product-price/provider-product-price.reducer';
import { IProvider } from 'app/shared/model/provider.model';
import { getEntities as getProviders } from 'app/entities/provider/provider.reducer';
import { getEntity, updateEntity, createEntity, reset } from './provider-product.reducer';
import { IProviderProduct } from 'app/shared/model/provider-product.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProviderProductUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const products = useAppSelector(state => state.product.entities);
  const batchProducts = useAppSelector(state => state.batchProduct.entities);
  const providerProductPrices = useAppSelector(state => state.providerProductPrice.entities);
  const providers = useAppSelector(state => state.provider.entities);
  const providerProductEntity = useAppSelector(state => state.providerProduct.entity);
  const loading = useAppSelector(state => state.providerProduct.loading);
  const updating = useAppSelector(state => state.providerProduct.updating);
  const updateSuccess = useAppSelector(state => state.providerProduct.updateSuccess);
  const handleClose = () => {
    props.history.push('/provider-product' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProducts({}));
    dispatch(getBatchProducts({}));
    dispatch(getProviderProductPrices({}));
    dispatch(getProviders({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...providerProductEntity,
      ...values,
      product: products.find(it => it.id.toString() === values.product.toString()),
      batchProduct: batchProducts.find(it => it.id.toString() === values.batchProduct.toString()),
      provider: providers.find(it => it.id.toString() === values.provider.toString()),
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
          ...providerProductEntity,
          product: providerProductEntity?.product?.id,
          batchProduct: providerProductEntity?.batchProduct?.id,
          provider: providerProductEntity?.provider?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.providerProduct.home.createOrEditLabel" data-cy="ProviderProductCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.providerProduct.home.createOrEditLabel">
              Create or edit a ProviderProduct
            </Translate>
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
                  id="provider-product-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.providerProduct.name')}
                id="provider-product-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.providerProduct.description')}
                id="provider-product-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="provider-product-product"
                name="product"
                data-cy="product"
                label={translate('managerProductStockApp.providerProduct.product')}
                type="select"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="provider-product-batchProduct"
                name="batchProduct"
                data-cy="batchProduct"
                label={translate('managerProductStockApp.providerProduct.batchProduct')}
                type="select"
              >
                <option value="" key="0" />
                {batchProducts
                  ? batchProducts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="provider-product-provider"
                name="provider"
                data-cy="provider"
                label={translate('managerProductStockApp.providerProduct.provider')}
                type="select"
                required
              >
                <option value="" key="0" />
                {providers
                  ? providers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/provider-product" replace color="info">
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

export default ProviderProductUpdate;
