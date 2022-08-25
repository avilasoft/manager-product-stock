import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './provider-item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProviderItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const providerItemEntity = useAppSelector(state => state.providerItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="providerItemDetailsHeading">
          <Translate contentKey="managerProductStockApp.providerItem.detail.title">ProviderItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{providerItemEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="managerProductStockApp.providerItem.code">Code</Translate>
            </span>
          </dt>
          <dd>{providerItemEntity.code}</dd>
          <dt>
            <span id="cost">
              <Translate contentKey="managerProductStockApp.providerItem.cost">Cost</Translate>
            </span>
          </dt>
          <dd>{providerItemEntity.cost}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.providerItem.description">Description</Translate>
            </span>
          </dt>
          <dd>{providerItemEntity.description}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.providerItem.provider">Provider</Translate>
          </dt>
          <dd>{providerItemEntity.provider ? providerItemEntity.provider.name : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.providerItem.unit">Unit</Translate>
          </dt>
          <dd>{providerItemEntity.unit ? providerItemEntity.unit.name : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.providerItem.item">Item</Translate>
          </dt>
          <dd>{providerItemEntity.item ? providerItemEntity.item.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/provider-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/provider-item/${providerItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProviderItemDetail;
