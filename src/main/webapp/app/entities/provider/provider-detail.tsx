import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './provider.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProviderDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const providerEntity = useAppSelector(state => state.provider.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="providerDetailsHeading">
          <Translate contentKey="managerProductStockApp.provider.detail.title">Provider</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{providerEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.provider.name">Name</Translate>
            </span>
          </dt>
          <dd>{providerEntity.name}</dd>
          <dt>
            <span id="descrption">
              <Translate contentKey="managerProductStockApp.provider.descrption">Descrption</Translate>
            </span>
          </dt>
          <dd>{providerEntity.descrption}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="managerProductStockApp.provider.email">Email</Translate>
            </span>
          </dt>
          <dd>{providerEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="managerProductStockApp.provider.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{providerEntity.phone}</dd>
          <dt>
            <span id="addressLine1">
              <Translate contentKey="managerProductStockApp.provider.addressLine1">Address Line 1</Translate>
            </span>
          </dt>
          <dd>{providerEntity.addressLine1}</dd>
          <dt>
            <span id="addressLine2">
              <Translate contentKey="managerProductStockApp.provider.addressLine2">Address Line 2</Translate>
            </span>
          </dt>
          <dd>{providerEntity.addressLine2}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="managerProductStockApp.provider.city">City</Translate>
            </span>
          </dt>
          <dd>{providerEntity.city}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="managerProductStockApp.provider.country">Country</Translate>
            </span>
          </dt>
          <dd>{providerEntity.country}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.provider.user">User</Translate>
          </dt>
          <dd>{providerEntity.user ? providerEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/provider" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/provider/${providerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProviderDetail;
