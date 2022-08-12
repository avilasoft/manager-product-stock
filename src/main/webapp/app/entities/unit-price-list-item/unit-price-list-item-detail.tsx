import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './unit-price-list-item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UnitPriceListItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const unitPriceListItemEntity = useAppSelector(state => state.unitPriceListItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="unitPriceListItemDetailsHeading">
          <Translate contentKey="managerProductStockApp.unitPriceListItem.detail.title">UnitPriceListItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{unitPriceListItemEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="managerProductStockApp.unitPriceListItem.code">Code</Translate>
            </span>
          </dt>
          <dd>{unitPriceListItemEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.unitPriceListItem.name">Name</Translate>
            </span>
          </dt>
          <dd>{unitPriceListItemEntity.name}</dd>
          <dt>
            <span id="unitCostTotal">
              <Translate contentKey="managerProductStockApp.unitPriceListItem.unitCostTotal">Unit Cost Total</Translate>
            </span>
          </dt>
          <dd>{unitPriceListItemEntity.unitCostTotal}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.unitPriceListItem.projectItemListItem">Project Item List Item</Translate>
          </dt>
          <dd>{unitPriceListItemEntity.projectItemListItem ? unitPriceListItemEntity.projectItemListItem.name : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.unitPriceListItem.unitPriceList">Unit Price List</Translate>
          </dt>
          <dd>{unitPriceListItemEntity.unitPriceList ? unitPriceListItemEntity.unitPriceList.code : ''}</dd>
        </dl>
        <Button tag={Link} to="/unit-price-list-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/unit-price-list-item/${unitPriceListItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UnitPriceListItemDetail;
