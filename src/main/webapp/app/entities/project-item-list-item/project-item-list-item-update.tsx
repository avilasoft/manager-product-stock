import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUnitPriceListItem } from 'app/shared/model/unit-price-list-item.model';
import { getEntities as getUnitPriceListItems } from 'app/entities/unit-price-list-item/unit-price-list-item.reducer';
import { IProjectItemList } from 'app/shared/model/project-item-list.model';
import { getEntities as getProjectItemLists } from 'app/entities/project-item-list/project-item-list.reducer';
import { IItem } from 'app/shared/model/item.model';
import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { IProviderItem } from 'app/shared/model/provider-item.model';
import { getEntities as getProviderItems } from 'app/entities/provider-item/provider-item.reducer';
import { IUnit } from 'app/shared/model/unit.model';
import { getEntities as getUnits } from 'app/entities/unit/unit.reducer';
import { getEntity, updateEntity, createEntity, reset } from './project-item-list-item.reducer';
import { IProjectItemListItem } from 'app/shared/model/project-item-list-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProjectItemListItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const unitPriceListItems = useAppSelector(state => state.unitPriceListItem.entities);
  const projectItemLists = useAppSelector(state => state.projectItemList.entities);
  const items = useAppSelector(state => state.item.entities);
  const providerItems = useAppSelector(state => state.providerItem.entities);
  const units = useAppSelector(state => state.unit.entities);
  const projectItemListItemEntity = useAppSelector(state => state.projectItemListItem.entity);
  const loading = useAppSelector(state => state.projectItemListItem.loading);
  const updating = useAppSelector(state => state.projectItemListItem.updating);
  const updateSuccess = useAppSelector(state => state.projectItemListItem.updateSuccess);
  const handleClose = () => {
    props.history.push('/project-item-list-item' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUnitPriceListItems({}));
    dispatch(getProjectItemLists({}));
    dispatch(getItems({}));
    dispatch(getProviderItems({}));
    dispatch(getUnits({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...projectItemListItemEntity,
      ...values,
      unitPriceListItem: unitPriceListItems.find(it => it.id.toString() === values.unitPriceListItem.toString()),
      projectItemList: projectItemLists.find(it => it.id.toString() === values.projectItemList.toString()),
      item: items.find(it => it.id.toString() === values.item.toString()),
      providerItem: providerItems.find(it => it.id.toString() === values.providerItem.toString()),
      unit: units.find(it => it.id.toString() === values.unit.toString()),
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
          ...projectItemListItemEntity,
          unitPriceListItem: projectItemListItemEntity?.unitPriceListItem?.id,
          projectItemList: projectItemListItemEntity?.projectItemList?.id,
          item: projectItemListItemEntity?.item?.id,
          providerItem: projectItemListItemEntity?.providerItem?.id,
          unit: projectItemListItemEntity?.unit?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.projectItemListItem.home.createOrEditLabel" data-cy="ProjectItemListItemCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.projectItemListItem.home.createOrEditLabel">
              Create or edit a ProjectItemListItem
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
                  id="project-item-list-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.projectItemListItem.code')}
                id="project-item-list-item-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.projectItemListItem.dimension')}
                id="project-item-list-item-dimension"
                name="dimension"
                data-cy="dimension"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.projectItemListItem.quantity')}
                id="project-item-list-item-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.projectItemListItem.description')}
                id="project-item-list-item-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="project-item-list-item-unitPriceListItem"
                name="unitPriceListItem"
                data-cy="unitPriceListItem"
                label={translate('managerProductStockApp.projectItemListItem.unitPriceListItem')}
                type="select"
              >
                <option value="" key="0" />
                {unitPriceListItems
                  ? unitPriceListItems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.unitPriceTotal}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="project-item-list-item-projectItemList"
                name="projectItemList"
                data-cy="projectItemList"
                label={translate('managerProductStockApp.projectItemListItem.projectItemList')}
                type="select"
                required
              >
                <option value="" key="0" />
                {projectItemLists
                  ? projectItemLists.map(otherEntity => (
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
                id="project-item-list-item-item"
                name="item"
                data-cy="item"
                label={translate('managerProductStockApp.projectItemListItem.item')}
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
              <ValidatedField
                id="project-item-list-item-providerItem"
                name="providerItem"
                data-cy="providerItem"
                label={translate('managerProductStockApp.projectItemListItem.providerItem')}
                type="select"
              >
                <option value="" key="0" />
                {providerItems
                  ? providerItems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.code}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="project-item-list-item-unit"
                name="unit"
                data-cy="unit"
                label={translate('managerProductStockApp.projectItemListItem.unit')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/project-item-list-item" replace color="info">
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

export default ProjectItemListItemUpdate;
