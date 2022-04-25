import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUnitType } from 'app/shared/model/unit-type.model';
import { getEntities as getUnitTypes } from 'app/entities/unit-type/unit-type.reducer';
import { IProviderProductPrice } from 'app/shared/model/provider-product-price.model';
import { getEntities as getProviderProductPrices } from 'app/entities/provider-product-price/provider-product-price.reducer';
import { getEntities as getUnits } from 'app/entities/unit/unit.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { getEntity, updateEntity, createEntity, reset } from './unit.reducer';
import { IUnit } from 'app/shared/model/unit.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UnitUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const unitTypes = useAppSelector(state => state.unitType.entities);
  const providerProductPrices = useAppSelector(state => state.providerProductPrice.entities);
  const units = useAppSelector(state => state.unit.entities);
  const products = useAppSelector(state => state.product.entities);
  const unitEntity = useAppSelector(state => state.unit.entity);
  const loading = useAppSelector(state => state.unit.loading);
  const updating = useAppSelector(state => state.unit.updating);
  const updateSuccess = useAppSelector(state => state.unit.updateSuccess);
  const handleClose = () => {
    props.history.push('/unit' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUnitTypes({}));
    dispatch(getProviderProductPrices({}));
    dispatch(getUnits({}));
    dispatch(getProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...unitEntity,
      ...values,
      parents: mapIdList(values.parents),
      unitType: unitTypes.find(it => it.id.toString() === values.unitType.toString()),
      providerProductPrice: providerProductPrices.find(it => it.id.toString() === values.providerProductPrice.toString()),
      product: products.find(it => it.id.toString() === values.product.toString()),
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
          ...unitEntity,
          unitType: unitEntity?.unitType?.id,
          providerProductPrice: unitEntity?.providerProductPrice?.id,
          parents: unitEntity?.parents?.map(e => e.id.toString()),
          product: unitEntity?.product?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.unit.home.createOrEditLabel" data-cy="UnitCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.unit.home.createOrEditLabel">Create or edit a Unit</Translate>
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
                  id="unit-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.unit.name')}
                id="unit-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.unit.description')}
                id="unit-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.unit.symbol')}
                id="unit-symbol"
                name="symbol"
                data-cy="symbol"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.unit.isBase')}
                id="unit-isBase"
                name="isBase"
                data-cy="isBase"
                check
                type="checkbox"
              />
              <ValidatedField
                id="unit-unitType"
                name="unitType"
                data-cy="unitType"
                label={translate('managerProductStockApp.unit.unitType')}
                type="select"
              >
                <option value="" key="0" />
                {unitTypes
                  ? unitTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="unit-providerProductPrice"
                name="providerProductPrice"
                data-cy="providerProductPrice"
                label={translate('managerProductStockApp.unit.providerProductPrice')}
                type="select"
              >
                <option value="" key="0" />
                {providerProductPrices
                  ? providerProductPrices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.price}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('managerProductStockApp.unit.parent')}
                id="unit-parent"
                data-cy="parent"
                type="select"
                multiple
                name="parents"
              >
                <option value="" key="0" />
                {units
                  ? units.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="unit-product"
                name="product"
                data-cy="product"
                label={translate('managerProductStockApp.unit.product')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/unit" replace color="info">
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

export default UnitUpdate;
