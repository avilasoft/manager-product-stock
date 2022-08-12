import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './business-associate.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BusinessAssociateDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const businessAssociateEntity = useAppSelector(state => state.businessAssociate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="businessAssociateDetailsHeading">
          <Translate contentKey="managerProductStockApp.businessAssociate.detail.title">BusinessAssociate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{businessAssociateEntity.id}</dd>
          <dt>
            <span id="nickname">
              <Translate contentKey="managerProductStockApp.businessAssociate.nickname">Nickname</Translate>
            </span>
          </dt>
          <dd>{businessAssociateEntity.nickname}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.businessAssociate.name">Name</Translate>
            </span>
          </dt>
          <dd>{businessAssociateEntity.name}</dd>
          <dt>
            <span id="lastname">
              <Translate contentKey="managerProductStockApp.businessAssociate.lastname">Lastname</Translate>
            </span>
          </dt>
          <dd>{businessAssociateEntity.lastname}</dd>
          <dt>
            <span id="comercialName">
              <Translate contentKey="managerProductStockApp.businessAssociate.comercialName">Comercial Name</Translate>
            </span>
          </dt>
          <dd>{businessAssociateEntity.comercialName}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="managerProductStockApp.businessAssociate.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{businessAssociateEntity.phone}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="managerProductStockApp.businessAssociate.type">Type</Translate>
            </span>
          </dt>
          <dd>{businessAssociateEntity.type}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.businessAssociate.user">User</Translate>
          </dt>
          <dd>{businessAssociateEntity.user ? businessAssociateEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/business-associate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/business-associate/${businessAssociateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BusinessAssociateDetail;
