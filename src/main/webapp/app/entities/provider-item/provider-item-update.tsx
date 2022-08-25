import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IProvider } from 'app/shared/model/provider.model';
import { getEntities as getProviders } from 'app/entities/provider/provider.reducer';
import { IUnit } from 'app/shared/model/unit.model';
import { getEntities as getUnits } from 'app/entities/unit/unit.reducer';
import { IItem } from 'app/shared/model/item.model';
import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './provider-item.reducer';
import { IProviderItem } from 'app/shared/model/provider-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProviderItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const providers = useAppSelector(state => state.provider.entities);
  const units = useAppSelector(state => state.unit.entities);
  const items = useAppSelector(state => state.item.entities);
  const providerItemEntity = useAppSelector(state => state.providerItem.entity);
  const loading = useAppSelector(state => state.providerItem.loading);
  const updating = useAppSelector(state => state.providerItem.updating);
  const updateSuccess = useAppSelector(state => state.providerItem.updateSuccess);
  const handleClose = () => {
    props.history.push('/provider-item' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProviders({}));
    dispatch(getUnits({}));
    dispatch(getItems({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...providerItemEntity,
      ...values,
      provider: providers.find(it => it.id.toString() === values.provider.toString()),
      unit: units.find(it => it.id.toString() === values.unit.toString()),
      item: items.find(it => it.id.toString() === values.item.toString()),
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
          ...providerItemEntity,
          provider: providerItemEntity?.provider?.id,
          unit: providerItemEntity?.unit?.id,
          item: providerItemEntity?.item?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.providerItem.home.createOrEditLabel" data-cy="ProviderItemCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.providerItem.home.createOrEditLabel">Create or edit a ProviderItem</Translate>
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
                  id="provider-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.providerItem.code')}
                id="provider-item-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.providerItem.cost')}
                id="provider-item-cost"
                name="cost"
                data-cy="cost"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.providerItem.description')}
                id="provider-item-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="provider-item-provider"
                name="provider"
                data-cy="provider"
                label={translate('managerProductStockApp.providerItem.provider')}
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
              <ValidatedField
                id="provider-item-unit"
                name="unit"
                data-cy="unit"
                label={translate('managerProductStockApp.providerItem.unit')}
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
                id="provider-item-item"
                name="item"
                data-cy="item"
                label={translate('managerProductStockApp.providerItem.item')}
                type="select"
                required
              >
                <option value="" key="0" />
                {items
                  ? items.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/provider-item" replace color="info">
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

export default ProviderItemUpdate;
