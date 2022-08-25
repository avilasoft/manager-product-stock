import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUnitPriceList } from 'app/shared/model/unit-price-list.model';
import { getEntities as getUnitPriceLists } from 'app/entities/unit-price-list/unit-price-list.reducer';
import { IComputedPriceList } from 'app/shared/model/computed-price-list.model';
import { getEntities as getComputedPriceLists } from 'app/entities/computed-price-list/computed-price-list.reducer';
import { IProject } from 'app/shared/model/project.model';
import { getEntities as getProjects } from 'app/entities/project/project.reducer';
import { getEntity, updateEntity, createEntity, reset } from './project-item-list.reducer';
import { IProjectItemList } from 'app/shared/model/project-item-list.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { ProjectItemListType } from 'app/shared/model/enumerations/project-item-list-type.model';
import { ProjectItemListStatus } from 'app/shared/model/enumerations/project-item-list-status.model';

export const ProjectItemListUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const unitPriceLists = useAppSelector(state => state.unitPriceList.entities);
  const computedPriceLists = useAppSelector(state => state.computedPriceList.entities);
  const projects = useAppSelector(state => state.project.entities);
  const projectItemListEntity = useAppSelector(state => state.projectItemList.entity);
  const loading = useAppSelector(state => state.projectItemList.loading);
  const updating = useAppSelector(state => state.projectItemList.updating);
  const updateSuccess = useAppSelector(state => state.projectItemList.updateSuccess);
  const projectItemListTypeValues = Object.keys(ProjectItemListType);
  const projectItemListStatusValues = Object.keys(ProjectItemListStatus);
  const handleClose = () => {
    props.history.push('/project-item-list' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUnitPriceLists({}));
    dispatch(getComputedPriceLists({}));
    dispatch(getProjects({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...projectItemListEntity,
      ...values,
      unitPriceList: unitPriceLists.find(it => it.id.toString() === values.unitPriceList.toString()),
      computedPriceList: computedPriceLists.find(it => it.id.toString() === values.computedPriceList.toString()),
      project: projects.find(it => it.id.toString() === values.project.toString()),
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
          type: 'EXCAVACION',
          status: 'DRAFT',
          ...projectItemListEntity,
          unitPriceList: projectItemListEntity?.unitPriceList?.id,
          computedPriceList: projectItemListEntity?.computedPriceList?.id,
          project: projectItemListEntity?.project?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.projectItemList.home.createOrEditLabel" data-cy="ProjectItemListCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.projectItemList.home.createOrEditLabel">
              Create or edit a ProjectItemList
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
                  id="project-item-list-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.projectItemList.name')}
                id="project-item-list-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.projectItemList.description')}
                id="project-item-list-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('managerProductStockApp.projectItemList.type')}
                id="project-item-list-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {projectItemListTypeValues.map(projectItemListType => (
                  <option value={projectItemListType} key={projectItemListType}>
                    {translate('managerProductStockApp.ProjectItemListType.' + projectItemListType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('managerProductStockApp.projectItemList.status')}
                id="project-item-list-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {projectItemListStatusValues.map(projectItemListStatus => (
                  <option value={projectItemListStatus} key={projectItemListStatus}>
                    {translate('managerProductStockApp.ProjectItemListStatus.' + projectItemListStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="project-item-list-unitPriceList"
                name="unitPriceList"
                data-cy="unitPriceList"
                label={translate('managerProductStockApp.projectItemList.unitPriceList')}
                type="select"
              >
                <option value="" key="0" />
                {unitPriceLists
                  ? unitPriceLists.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.unitPriceListTotal}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="project-item-list-computedPriceList"
                name="computedPriceList"
                data-cy="computedPriceList"
                label={translate('managerProductStockApp.projectItemList.computedPriceList')}
                type="select"
              >
                <option value="" key="0" />
                {computedPriceLists
                  ? computedPriceLists.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.computedPriceListTotal}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="project-item-list-project"
                name="project"
                data-cy="project"
                label={translate('managerProductStockApp.projectItemList.project')}
                type="select"
                required
              >
                <option value="" key="0" />
                {projects
                  ? projects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/project-item-list" replace color="info">
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

export default ProjectItemListUpdate;
