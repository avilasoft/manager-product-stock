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
            <span id="description">
              <Translate contentKey="managerProductStockApp.provider.description">Description</Translate>
            </span>
          </dt>
          <dd>{providerEntity.description}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.provider.businessAssociate">Business Associate</Translate>
          </dt>
          <dd>{providerEntity.businessAssociate ? providerEntity.businessAssociate.comercialName : ''}</dd>
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
