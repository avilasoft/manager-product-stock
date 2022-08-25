import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './computed-price-list.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ComputedPriceListDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const computedPriceListEntity = useAppSelector(state => state.computedPriceList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="computedPriceListDetailsHeading">
          <Translate contentKey="managerProductStockApp.computedPriceList.detail.title">ComputedPriceList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{computedPriceListEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="managerProductStockApp.computedPriceList.code">Code</Translate>
            </span>
          </dt>
          <dd>{computedPriceListEntity.code}</dd>
          <dt>
            <span id="computedPriceListTotal">
              <Translate contentKey="managerProductStockApp.computedPriceList.computedPriceListTotal">Computed Price List Total</Translate>
            </span>
          </dt>
          <dd>{computedPriceListEntity.computedPriceListTotal}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.computedPriceList.description">Description</Translate>
            </span>
          </dt>
          <dd>{computedPriceListEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/computed-price-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/computed-price-list/${computedPriceListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ComputedPriceListDetail;
