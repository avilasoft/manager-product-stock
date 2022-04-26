import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities as getPriceHistories } from 'app/entities/price-history/price-history.reducer';
import { IPrice } from 'app/shared/model/price.model';
import { getEntities as getPrices } from 'app/entities/price/price.reducer';
import { getEntity, updateEntity, createEntity, reset } from './price-history.reducer';
import { IPriceHistory } from 'app/shared/model/price-history.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PriceHistoryUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const priceHistories = useAppSelector(state => state.priceHistory.entities);
  const prices = useAppSelector(state => state.price.entities);
  const priceHistoryEntity = useAppSelector(state => state.priceHistory.entity);
  const loading = useAppSelector(state => state.priceHistory.loading);
  const updating = useAppSelector(state => state.priceHistory.updating);
  const updateSuccess = useAppSelector(state => state.priceHistory.updateSuccess);
  const handleClose = () => {
    props.history.push('/price-history' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPriceHistories({}));
    dispatch(getPrices({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...priceHistoryEntity,
      ...values,
      parent: priceHistories.find(it => it.id.toString() === values.parent.toString()),
      price: prices.find(it => it.id.toString() === values.price.toString()),
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
          ...priceHistoryEntity,
          parent: priceHistoryEntity?.parent?.id,
          price: priceHistoryEntity?.price?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.priceHistory.home.createOrEditLabel" data-cy="PriceHistoryCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.priceHistory.home.createOrEditLabel">Create or edit a PriceHistory</Translate>
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
                  id="price-history-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.priceHistory.name')}
                id="price-history-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.priceHistory.description')}
                id="price-history-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="price-history-parent"
                name="parent"
                data-cy="parent"
                label={translate('managerProductStockApp.priceHistory.parent')}
                type="select"
              >
                <option value="" key="0" />
                {priceHistories
                  ? priceHistories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="price-history-price"
                name="price"
                data-cy="price"
                label={translate('managerProductStockApp.priceHistory.price')}
                type="select"
                required
              >
                <option value="" key="0" />
                {prices
                  ? prices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/price-history" replace color="info">
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

export default PriceHistoryUpdate;
