import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './batch-product.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BatchProductDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const batchProductEntity = useAppSelector(state => state.batchProduct.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="batchProductDetailsHeading">
          <Translate contentKey="managerProductStockApp.batchProduct.detail.title">BatchProduct</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{batchProductEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.batchProduct.name">Name</Translate>
            </span>
          </dt>
          <dd>{batchProductEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.batchProduct.description">Description</Translate>
            </span>
          </dt>
          <dd>{batchProductEntity.description}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.batchProduct.product">Product</Translate>
          </dt>
          <dd>{batchProductEntity.product ? batchProductEntity.product.name : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.batchProduct.batch">Batch</Translate>
          </dt>
          <dd>{batchProductEntity.batch ? batchProductEntity.batch.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/batch-product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/batch-product/${batchProductEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BatchProductDetail;
