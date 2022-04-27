import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './provider-product-price.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProviderProductPriceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const providerProductPriceEntity = useAppSelector(state => state.providerProductPrice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="providerProductPriceDetailsHeading">
          <Translate contentKey="managerProductStockApp.providerProductPrice.detail.title">ProviderProductPrice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{providerProductPriceEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.providerProductPrice.name">Name</Translate>
            </span>
          </dt>
          <dd>{providerProductPriceEntity.name}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="managerProductStockApp.providerProductPrice.value">Value</Translate>
            </span>
          </dt>
          <dd>{providerProductPriceEntity.value}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.providerProductPrice.price">Price</Translate>
          </dt>
          <dd>{providerProductPriceEntity.price ? providerProductPriceEntity.price.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/provider-product-price" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/provider-product-price/${providerProductPriceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProviderProductPriceDetail;
