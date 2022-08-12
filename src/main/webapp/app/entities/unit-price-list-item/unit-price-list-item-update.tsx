import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IProjectItemListItem } from 'app/shared/model/project-item-list-item.model';
import { getEntities as getProjectItemListItems } from 'app/entities/project-item-list-item/project-item-list-item.reducer';
import { IUnitPriceList } from 'app/shared/model/unit-price-list.model';
import { getEntities as getUnitPriceLists } from 'app/entities/unit-price-list/unit-price-list.reducer';
import { getEntity, updateEntity, createEntity, reset } from './unit-price-list-item.reducer';
import { IUnitPriceListItem } from 'app/shared/model/unit-price-list-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UnitPriceListItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const projectItemListItems = useAppSelector(state => state.projectItemListItem.entities);
  const unitPriceLists = useAppSelector(state => state.unitPriceList.entities);
  const unitPriceListItemEntity = useAppSelector(state => state.unitPriceListItem.entity);
  const loading = useAppSelector(state => state.unitPriceListItem.loading);
  const updating = useAppSelector(state => state.unitPriceListItem.updating);
  const updateSuccess = useAppSelector(state => state.unitPriceListItem.updateSuccess);
  const handleClose = () => {
    props.history.push('/unit-price-list-item' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProjectItemListItems({}));
    dispatch(getUnitPriceLists({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...unitPriceListItemEntity,
      ...values,
      projectItemListItem: projectItemListItems.find(it => it.id.toString() === values.projectItemListItem.toString()),
      unitPriceList: unitPriceLists.find(it => it.id.toString() === values.unitPriceList.toString()),
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
          ...unitPriceListItemEntity,
          projectItemListItem: unitPriceListItemEntity?.projectItemListItem?.id,
          unitPriceList: unitPriceListItemEntity?.unitPriceList?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.unitPriceListItem.home.createOrEditLabel" data-cy="UnitPriceListItemCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.unitPriceListItem.home.createOrEditLabel">
              Create or edit a UnitPriceListItem
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
                  id="unit-price-list-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.unitPriceListItem.code')}
                id="unit-price-list-item-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.unitPriceListItem.name')}
                id="unit-price-list-item-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('managerProductStockApp.unitPriceListItem.unitCostTotal')}
                id="unit-price-list-item-unitCostTotal"
                name="unitCostTotal"
                data-cy="unitCostTotal"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="unit-price-list-item-projectItemListItem"
                name="projectItemListItem"
                data-cy="projectItemListItem"
                label={translate('managerProductStockApp.unitPriceListItem.projectItemListItem')}
                type="select"
                required
              >
                <option value="" key="0" />
                {projectItemListItems
                  ? projectItemListItems.map(otherEntity => (
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
                id="unit-price-list-item-unitPriceList"
                name="unitPriceList"
                data-cy="unitPriceList"
                label={translate('managerProductStockApp.unitPriceListItem.unitPriceList')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/unit-price-list-item" replace color="info">
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

export default UnitPriceListItemUpdate;
