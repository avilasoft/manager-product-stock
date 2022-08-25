import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './computed-price-list-item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ComputedPriceListItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const computedPriceListItemEntity = useAppSelector(state => state.computedPriceListItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="computedPriceListItemDetailsHeading">
          <Translate contentKey="managerProductStockApp.computedPriceListItem.detail.title">ComputedPriceListItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{computedPriceListItemEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="managerProductStockApp.computedPriceListItem.code">Code</Translate>
            </span>
          </dt>
          <dd>{computedPriceListItemEntity.code}</dd>
          <dt>
            <span id="computedPriceTotal">
              <Translate contentKey="managerProductStockApp.computedPriceListItem.computedPriceTotal">Computed Price Total</Translate>
            </span>
          </dt>
          <dd>{computedPriceListItemEntity.computedPriceTotal}</dd>
          <dt>
            <span id="computedQuantityTotal">
              <Translate contentKey="managerProductStockApp.computedPriceListItem.computedQuantityTotal">Computed Quantity Total</Translate>
            </span>
          </dt>
          <dd>{computedPriceListItemEntity.computedQuantityTotal}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.computedPriceListItem.description">Description</Translate>
            </span>
          </dt>
          <dd>{computedPriceListItemEntity.description}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.computedPriceListItem.unitPriceList">Unit Price List</Translate>
          </dt>
          <dd>{computedPriceListItemEntity.unitPriceList ? computedPriceListItemEntity.unitPriceList.unitPriceListTotal : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.computedPriceListItem.computedPriceList">Computed Price List</Translate>
          </dt>
          <dd>{computedPriceListItemEntity.computedPriceList ? computedPriceListItemEntity.computedPriceList.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/computed-price-list-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/computed-price-list-item/${computedPriceListItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ComputedPriceListItemDetail;
