import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './unit-price-list.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UnitPriceListDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const unitPriceListEntity = useAppSelector(state => state.unitPriceList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="unitPriceListDetailsHeading">
          <Translate contentKey="managerProductStockApp.unitPriceList.detail.title">UnitPriceList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{unitPriceListEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="managerProductStockApp.unitPriceList.code">Code</Translate>
            </span>
          </dt>
          <dd>{unitPriceListEntity.code}</dd>
          <dt>
            <span id="unitPriceListTotal">
              <Translate contentKey="managerProductStockApp.unitPriceList.unitPriceListTotal">Unit Price List Total</Translate>
            </span>
          </dt>
          <dd>{unitPriceListEntity.unitPriceListTotal}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.unitPriceList.description">Description</Translate>
            </span>
          </dt>
          <dd>{unitPriceListEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/unit-price-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/unit-price-list/${unitPriceListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UnitPriceListDetail;
