import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './provider-product.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProviderProductDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const providerProductEntity = useAppSelector(state => state.providerProduct.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="providerProductDetailsHeading">
          <Translate contentKey="managerProductStockApp.providerProduct.detail.title">ProviderProduct</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{providerProductEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.providerProduct.name">Name</Translate>
            </span>
          </dt>
          <dd>{providerProductEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.providerProduct.description">Description</Translate>
            </span>
          </dt>
          <dd>{providerProductEntity.description}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.providerProduct.provider">Provider</Translate>
          </dt>
          <dd>{providerProductEntity.provider ? providerProductEntity.provider.name : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.providerProduct.providerProductPrice">Provider Product Price</Translate>
          </dt>
          <dd>{providerProductEntity.providerProductPrice ? providerProductEntity.providerProductPrice.name : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.providerProduct.product">Product</Translate>
          </dt>
          <dd>{providerProductEntity.product ? providerProductEntity.product.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/provider-product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/provider-product/${providerProductEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProviderProductDetail;
