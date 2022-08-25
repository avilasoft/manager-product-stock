import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './project-item-list-item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProjectItemListItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const projectItemListItemEntity = useAppSelector(state => state.projectItemListItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="projectItemListItemDetailsHeading">
          <Translate contentKey="managerProductStockApp.projectItemListItem.detail.title">ProjectItemListItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{projectItemListItemEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="managerProductStockApp.projectItemListItem.code">Code</Translate>
            </span>
          </dt>
          <dd>{projectItemListItemEntity.code}</dd>
          <dt>
            <span id="dimension">
              <Translate contentKey="managerProductStockApp.projectItemListItem.dimension">Dimension</Translate>
            </span>
          </dt>
          <dd>{projectItemListItemEntity.dimension}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="managerProductStockApp.projectItemListItem.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{projectItemListItemEntity.quantity}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.projectItemListItem.description">Description</Translate>
            </span>
          </dt>
          <dd>{projectItemListItemEntity.description}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.projectItemListItem.unitPriceListItem">Unit Price List Item</Translate>
          </dt>
          <dd>{projectItemListItemEntity.unitPriceListItem ? projectItemListItemEntity.unitPriceListItem.unitPriceTotal : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.projectItemListItem.projectItemList">Project Item List</Translate>
          </dt>
          <dd>{projectItemListItemEntity.projectItemList ? projectItemListItemEntity.projectItemList.name : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.projectItemListItem.item">Item</Translate>
          </dt>
          <dd>{projectItemListItemEntity.item ? projectItemListItemEntity.item.name : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.projectItemListItem.providerItem">Provider Item</Translate>
          </dt>
          <dd>{projectItemListItemEntity.providerItem ? projectItemListItemEntity.providerItem.code : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.projectItemListItem.unit">Unit</Translate>
          </dt>
          <dd>{projectItemListItemEntity.unit ? projectItemListItemEntity.unit.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/project-item-list-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/project-item-list-item/${projectItemListItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProjectItemListItemDetail;
