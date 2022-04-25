import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IProviderProduct } from 'app/shared/model/provider-product.model';
import { getEntities as getProviderProducts } from 'app/entities/provider-product/provider-product.reducer';
import { getEntity, updateEntity, createEntity, reset } from './provider-product-price.reducer';
import { IProviderProductPrice } from 'app/shared/model/provider-product-price.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProviderProductPriceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const providerProducts = useAppSelector(state => state.providerProduct.entities);
  const providerProductPriceEntity = useAppSelector(state => state.providerProductPrice.entity);
  const loading = useAppSelector(state => state.providerProductPrice.loading);
  const updating = useAppSelector(state => state.providerProductPrice.updating);
  const updateSuccess = useAppSelector(state => state.providerProductPrice.updateSuccess);
  const handleClose = () => {
    props.history.push('/provider-product-price' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProviderProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...providerProductPriceEntity,
      ...values,
      providerProduct: providerProducts.find(it => it.id.toString() === values.providerProduct.toString()),
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
          ...providerProductPriceEntity,
          providerProduct: providerProductPriceEntity?.providerProduct?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.providerProductPrice.home.createOrEditLabel" data-cy="ProviderProductPriceCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.providerProductPrice.home.createOrEditLabel">
              Create or edit a ProviderProductPrice
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
                  id="provider-product-price-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.providerProductPrice.price')}
                id="provider-product-price-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                id="provider-product-price-providerProduct"
                name="providerProduct"
                data-cy="providerProduct"
                label={translate('managerProductStockApp.providerProductPrice.providerProduct')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/provider-product-price" replace color="info">
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

export default ProviderProductPriceUpdate;
