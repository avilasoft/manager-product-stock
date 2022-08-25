import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './project-item-list.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProjectItemListDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const projectItemListEntity = useAppSelector(state => state.projectItemList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="projectItemListDetailsHeading">
          <Translate contentKey="managerProductStockApp.projectItemList.detail.title">ProjectItemList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{projectItemListEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.projectItemList.name">Name</Translate>
            </span>
          </dt>
          <dd>{projectItemListEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.projectItemList.description">Description</Translate>
            </span>
          </dt>
          <dd>{projectItemListEntity.description}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="managerProductStockApp.projectItemList.type">Type</Translate>
            </span>
          </dt>
          <dd>{projectItemListEntity.type}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="managerProductStockApp.projectItemList.status">Status</Translate>
            </span>
          </dt>
          <dd>{projectItemListEntity.status}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.projectItemList.unitPriceList">Unit Price List</Translate>
          </dt>
          <dd>{projectItemListEntity.unitPriceList ? projectItemListEntity.unitPriceList.unitPriceListTotal : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.projectItemList.computedPriceList">Computed Price List</Translate>
          </dt>
          <dd>{projectItemListEntity.computedPriceList ? projectItemListEntity.computedPriceList.computedPriceListTotal : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.projectItemList.project">Project</Translate>
          </dt>
          <dd>{projectItemListEntity.project ? projectItemListEntity.project.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/project-item-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/project-item-list/${projectItemListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProjectItemListDetail;
