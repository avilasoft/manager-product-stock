import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUnitPriceList } from 'app/shared/model/unit-price-list.model';
import { getEntities as getUnitPriceLists } from 'app/entities/unit-price-list/unit-price-list.reducer';
import { IComputedPriceList } from 'app/shared/model/computed-price-list.model';
import { getEntities as getComputedPriceLists } from 'app/entities/computed-price-list/computed-price-list.reducer';
import { getEntity, updateEntity, createEntity, reset } from './computed-price-list-item.reducer';
import { IComputedPriceListItem } from 'app/shared/model/computed-price-list-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ComputedPriceListItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const unitPriceLists = useAppSelector(state => state.unitPriceList.entities);
  const computedPriceLists = useAppSelector(state => state.computedPriceList.entities);
  const computedPriceListItemEntity = useAppSelector(state => state.computedPriceListItem.entity);
  const loading = useAppSelector(state => state.computedPriceListItem.loading);
  const updating = useAppSelector(state => state.computedPriceListItem.updating);
  const updateSuccess = useAppSelector(state => state.computedPriceListItem.updateSuccess);
  const handleClose = () => {
    props.history.push('/computed-price-list-item' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUnitPriceLists({}));
    dispatch(getComputedPriceLists({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...computedPriceListItemEntity,
      ...values,
      unitPriceList: unitPriceLists.find(it => it.id.toString() === values.unitPriceList.toString()),
      computedPriceList: computedPriceLists.find(it => it.id.toString() === values.computedPriceList.toString()),
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
          ...computedPriceListItemEntity,
          unitPriceList: computedPriceListItemEntity?.unitPriceList?.id,
          computedPriceList: computedPriceListItemEntity?.computedPriceList?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.computedPriceListItem.home.createOrEditLabel" data-cy="ComputedPriceListItemCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.computedPriceListItem.home.createOrEditLabel">
              Create or edit a ComputedPriceListItem
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
                  id="computed-price-list-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.computedPriceListItem.code')}
                id="computed-price-list-item-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.computedPriceListItem.name')}
                id="computed-price-list-item-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('managerProductStockApp.computedPriceListItem.computedCostTotal')}
                id="computed-price-list-item-computedCostTotal"
                name="computedCostTotal"
                data-cy="computedCostTotal"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.computedPriceListItem.computedQuantityTotal')}
                id="computed-price-list-item-computedQuantityTotal"
                name="computedQuantityTotal"
                data-cy="computedQuantityTotal"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="computed-price-list-item-unitPriceList"
                name="unitPriceList"
                data-cy="unitPriceList"
                label={translate('managerProductStockApp.computedPriceListItem.unitPriceList')}
                type="select"
                required
              >
                <option value="" key="0" />
                {unitPriceLists
                  ? unitPriceLists.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.code}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="computed-price-list-item-computedPriceList"
                name="computedPriceList"
                data-cy="computedPriceList"
                label={translate('managerProductStockApp.computedPriceListItem.computedPriceList')}
                type="select"
                required
              >
                <option value="" key="0" />
                {computedPriceLists
                  ? computedPriceLists.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.code}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/computed-price-list-item" replace color="info">
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

export default ComputedPriceListItemUpdate;
